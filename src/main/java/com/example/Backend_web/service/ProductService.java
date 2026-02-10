package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.ProductRequest;
import com.example.Backend_web.dto.request.ProductVariantRequest;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.dto.response.ProductSearchResponse;
import com.example.Backend_web.entity.*;
import com.example.Backend_web.repository.*;
import com.example.Backend_web.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final ProductMapper productMapper;
    private final ProductAttributeValueRepository attributeValueRepository; //
    private final ProductVariantRepository productVariantRepository;
    private final HotProductRepository hotProductRepository;



    // Thêm product từ ProductRequest, trả về ProductResponse
    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(request.getSlug()); // nhận trực tiếp từ client
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus() != null ? request.getStatus() : true);

        // Gán category
        product.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        // Tạo variants
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            List<ProductVariant> variants = request.getVariants().stream().map(v -> {
                ProductVariant variant = new ProductVariant();
                variant.setName(v.getName());
                variant.setPrice(v.getPrice());
                variant.setQuantity(v.getQuantity() != null ? v.getQuantity() : 0);
                variant.setStatus(true);

                // Gán color nếu có
                if (v.getColorId() != null) {
                    variant.setColor(colorRepository.findById(v.getColorId())
                            .orElseThrow(() -> new RuntimeException("Color not found")));
                }

                // Gán product cho variant
                variant.setProduct(product);

                // Gán images cho variant
                if (v.getImages() != null && !v.getImages().isEmpty()) {
                    List<ProductImage> images = v.getImages().stream().map(url -> {
                        ProductImage img = new ProductImage();
                        img.setImageUrl(url);
                        img.setVariant(variant);
                        return img;
                    }).collect(Collectors.toList());
                    variant.setImages(images);
                }

                //  Gán attributes cho variant
                if (v.getAttributes() != null && !v.getAttributes().isEmpty()) {
                    List<ProductVariantAttribute> variantAttributes = v.getAttributes().stream()
                            .map(attrReq -> {
//                                ProductVariantAttribute pva = new ProductVariantAttribute();
//                                pva.setProductVariant(variant);
//                                pva.setAttributeValue(attributeValueRepository.findById(attrReq.getAttributeId())
//                                        .orElseThrow(() -> new RuntimeException("AttributeValue not found")));
//                                pva.setValue(attrReq.getValue()); //  Gán giá trị thực tế user chọn

                                //Lấy theo attribute_id
                                ProductVariantAttribute pva = new ProductVariantAttribute();
                                pva.setProductVariant(variant);

// Lấy value thực tế đúng
                                ProductAttributeValue pav = attributeValueRepository.findByAttributeIdAndValue(
                                        attrReq.getAttributeId(), attrReq.getValue()
                                ).orElseThrow(() -> new RuntimeException("AttributeValue not found"));

                                pva.setAttributeValue(pav);
                                pva.setValue(attrReq.getValue()); // giá trị người dùng chọn

                                return pva;
                            }).collect(Collectors.toList());
                    variant.setVariantAttributes(variantAttributes);
                }

                return variant;
            }).collect(Collectors.toList());

            product.setVariants(variants);
        }


        // Lưu Product + Variants + Images nhờ Cascade
        Product saved = productRepository.save(product);

        return productMapper.toProductResponse(saved);
    }

    // Lấy tất cả products
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    // Lấy product theo id
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponse(product);
    }


    //Update thông tin sản phẩm
    public ProductResponse updateProduct(Integer id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update thông tin cơ bản
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());

        product.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        // Map variant cũ
        Map<Integer, ProductVariant> oldVariantMap = product.getVariants()
                .stream()
                .collect(Collectors.toMap(ProductVariant::getVariantId, v -> v));

        List<ProductVariant> keepList = new ArrayList<>();


        // 1) XỬ LÝ VARIANT CŨ HOẶC THÊM VARIANT MỚI

        for (ProductVariantRequest vReq : request.getVariants()) {

            ProductVariant variant;

            // ---------- UPDATE VARIANT CŨ ----------
            if (vReq.getVariantId() != null && oldVariantMap.containsKey(vReq.getVariantId())) {

                variant = oldVariantMap.get(vReq.getVariantId());

                variant.setName(vReq.getName());
                variant.setPrice(vReq.getPrice());
                variant.setQuantity(vReq.getQuantity());
                variant.setStatus(true);

                if (vReq.getColorId() != null) {
                    variant.setColor(colorRepository.findById(vReq.getColorId())
                            .orElseThrow(() -> new RuntimeException("Color not found")));
                }

                // Update hình ảnh sản phẩm (images)
                variant.getImages().clear();
                if (vReq.getImages() != null) {
                    vReq.getImages().forEach(url -> {
                        ProductImage img = new ProductImage();
                        img.setVariant(variant);
                        img.setImageUrl(url);
                        variant.getImages().add(img);
                    });
                }

                // Update thuộc tính (attributes)
//                variant.getVariantAttributes().clear();
//                if (vReq.getAttributes() != null) {
//                    vReq.getAttributes().forEach(a -> {
//                        ProductVariantAttribute attr = new ProductVariantAttribute();
//                        attr.setProductVariant(variant);
//                        attr.setAttributeValue(attributeValueRepository.findById(a.getAttributeId())
//                                .orElseThrow(() -> new RuntimeException("Attr not found")));
//                        attr.setValue(a.getValue());
//                        variant.getVariantAttributes().add(attr);
//                    });
//                }

                // Update thuộc tính (attributes)
                variant.getVariantAttributes().clear();
                if (vReq.getAttributes() != null) {
                    vReq.getAttributes().forEach(a -> {
                        ProductVariantAttribute attr = new ProductVariantAttribute();
                        attr.setProductVariant(variant);

                        // Tìm ProductAttributeValue dựa trên attribute_id + value
                        ProductAttributeValue pav = attributeValueRepository
                                .findByAttributeIdAndValue(a.getAttributeId(), a.getValue())
                                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));

                        attr.setAttributeValue(pav); // ← gán chính xác
                        attr.setValue(a.getValue()); // ← lưu giá trị thực tế user chọn
                        variant.getVariantAttributes().add(attr);
                    });
                }

            }

            // ---------- THÊM VARIANT MỚI ----------
            else {
                variant = new ProductVariant();
                variant.setProduct(product);
                variant.setName(vReq.getName());
                variant.setPrice(vReq.getPrice());
                variant.setQuantity(vReq.getQuantity());
                variant.setStatus(true);

                variant.setColor(colorRepository.findById(vReq.getColorId())
                        .orElseThrow(() -> new RuntimeException("Color not found")));

                // Image
                if (vReq.getImages() != null) {
                    List<ProductImage> imgs = vReq.getImages().stream().map(url -> {
                        ProductImage img = new ProductImage();
                        img.setVariant(variant);
                        img.setImageUrl(url);
                        return img;
                    }).toList();
                    variant.setImages(imgs);
                }

                // Attribute
                if (vReq.getAttributes() != null) {
                    List<ProductVariantAttribute> attrs = vReq.getAttributes().stream().map(a -> {
                        ProductVariantAttribute attr = new ProductVariantAttribute();
                        attr.setProductVariant(variant);
                        attr.setAttributeValue(attributeValueRepository.findById(a.getAttributeId())
                                .orElseThrow(() -> new RuntimeException("Attr not found")));
                        attr.setValue(a.getValue());
                        return attr;
                    }).toList();
                    variant.setVariantAttributes(attrs);
                }

                product.getVariants().add(variant); // ← THÊM VÀO LIST CŨ
            }

            keepList.add(variant);
        }


        // 2) XOÁ VARIANT KHÔNG CÓ TRONG REQUEST
        product.getVariants().removeIf(v -> !keepList.contains(v));

        //  KHÔNG BAO GIỜ GÁN LIST MỚI
        // product.setVariants(updatedVariantList);

        Product updated = productRepository.save(product);

        return productMapper.toProductResponse(updated);
    }





    // Xóa sản phẩm
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    // Lấy danh mục sản phẩm theo slug
    public List<ProductResponse> getProductsByCategorySlug(String slug) {
        return productRepository.findAllByCategorySlug(slug)
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết sản phẩm
    public ProductResponse getProductDetailBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm với slug: " + slug));
        return productMapper.toProductResponse(product);
    }

//    public ProductResponse getProductBySlug(String slug) {
//        Product product = productRepository.findBySlug(slug)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
//        return productMapper.toProductResponse(product);
//    }

    // Tìm kiếm thông minh + phân trang
    // ------------------ SMART SEARCH ------------------
    public List<ProductSearchResponse> searchSmart(String keyword, int page, int size) {

        int offset = page * size;

        // Tách keyword thành từng từ
        String[] terms = keyword.toLowerCase().trim().split("\\s+");

        // Ghép thành pattern cho AND logic
        String andPattern = Arrays.stream(terms)
                .map(t -> "%" + t + "%")
                .collect(Collectors.joining(" "));

        List<Product> products = productRepository.searchSmart(
                keyword.toLowerCase(),
                andPattern,
                terms.length,
                size,
                offset
        );

        return products.stream().map(p -> {

            ProductVariant variant =
                    (p.getVariants() != null && !p.getVariants().isEmpty())
                            ? p.getVariants().get(0)
                            : null;

            BigDecimal price = variant != null ? variant.getPrice() : BigDecimal.ZERO;
            String image = (variant != null && variant.getImages() != null && !variant.getImages().isEmpty())
                    ? variant.getImages().get(0).getImageUrl() : "";

            return ProductSearchResponse.builder()
                    .id(p.getProductId().longValue())
                    .name(p.getName())
                    .price(price)
                    .image(image)
                    .build();

        }).toList();
    }


    public List<String> autocomplete(String keyword) {
        return productRepository.autocomplete(keyword);
    }




//    // Tìm Sản phẩm theo giá Giảm dần
//    public List<ProductResponse> searchOrderByPriceDesc(Integer categoryId, String keyword) {
//        if (keyword == null) keyword = "";
//
//        return productRepository
//                .orderByPriceDesc(categoryId, keyword.trim())
//                .stream()
//                .map(productMapper::toProductResponse)
//                .toList();
//    }
//
//    // Tìm Sản phẩm theo giá Tăng dần
//    public List<ProductResponse> searchOrderByPriceAsc(Integer categoryId, String keyword) {
//        if (keyword == null) keyword = "";
//
//        return productRepository
//                .orderByPriceAsc(categoryId, keyword.trim())
//                .stream()
//                .map(productMapper::toProductResponse)
//                .toList();
//    }

    // Tìm Sản phẩm theo các tiêu chí (Giá SP tăng dần, giảm dần,...)
    public List<ProductResponse> searchProducts(
            Integer categoryId,
            String keyword,
            String sort
    ) {
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId is required");
        }

        String safeKeyword =
                (keyword == null || keyword.isBlank()) ? "" : keyword.trim();

        String safeSort =
                (sort == null || sort.isBlank())
                        ? "price_asc"
                        : sort.trim().toLowerCase();   //  QUAN TRỌNG

        List<Product> products;

        if ("price_desc".equals(safeSort)) {
            System.out.println(">>> DESC");
            products = productRepository.orderByPriceDesc(categoryId, safeKeyword);
        } else {
            System.out.println(">>> ASC");
            products = productRepository.orderByPriceAsc(categoryId, safeKeyword);
        }

        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    // Tìm Sản phẩm theo tiêu chí Dung lượng RAM
    public List<ProductResponse> filterByAttribute(String code, String value){
        if (code == null || value == null){
            throw new IllegalArgumentException("code or value is required");
        }

        List<Product> products = productRepository. filterByAttribute(code.trim(), value.trim());
        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    // Hiển thị danh sách Sản phẩm liên quan
    public List<ProductResponse> getRelatedProducts(Integer productId) {
        Product current = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Sản phẩm"));

        List<Product> related = productRepository.findRelatedByCategory(
                current.getCategory().getCategoryId(),
                productId
        );
        return related.stream()
                .limit(10)
                .map(productMapper::toProductResponse)
                .toList();
    }



}
