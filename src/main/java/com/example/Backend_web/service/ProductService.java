package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.ProductRequest;
import com.example.Backend_web.dto.request.ProductVariantRequest;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.ProductImage;
import com.example.Backend_web.entity.ProductVariant;
import com.example.Backend_web.entity.ProductVariantAttribute;
import com.example.Backend_web.repository.CategoryRepository;
import com.example.Backend_web.repository.ColorRepository;
import com.example.Backend_web.repository.ProductAttributeValueRepository;
import com.example.Backend_web.repository.ProductRepository;
import com.example.Backend_web.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final ProductMapper productMapper;
    private final ProductAttributeValueRepository attributeValueRepository; // ✅



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

                // ✅ Gán attributes cho variant
                if (v.getAttributes() != null && !v.getAttributes().isEmpty()) {
                    List<ProductVariantAttribute> variantAttributes = v.getAttributes().stream()
                            .map(attrReq -> {
                                ProductVariantAttribute pva = new ProductVariantAttribute();
                                pva.setProductVariant(variant);
                                pva.setAttributeValue(attributeValueRepository.findById(attrReq.getAttributeId())
                                        .orElseThrow(() -> new RuntimeException("AttributeValue not found")));
                                pva.setValue(attrReq.getValue()); // ✅ Gán giá trị thực tế user chọn
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

    // ==================== UPDATE ====================
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());

        // Gán category mới
        product.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        // Xóa variants cũ rồi thêm lại (cách đơn giản nhất)
        product.getVariants().clear();
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            List<ProductVariant> variants = request.getVariants().stream().map(v -> {
                ProductVariant variant = new ProductVariant();
                variant.setName(v.getName());
                variant.setPrice(v.getPrice());
                variant.setQuantity(v.getQuantity() != null ? v.getQuantity() : 0);
                variant.setStatus(true);

                if (v.getColorId() != null) {
                    variant.setColor(colorRepository.findById(v.getColorId())
                            .orElseThrow(() -> new RuntimeException("Color not found")));
                }

                variant.setProduct(product);

                if (v.getImages() != null && !v.getImages().isEmpty()) {
                    List<ProductImage> images = v.getImages().stream().map(url -> {
                        ProductImage img = new ProductImage();
                        img.setImageUrl(url);
                        img.setVariant(variant);
                        return img;
                    }).toList();
                    variant.setImages(images);
                }

                return variant;
            }).toList();

            product.getVariants().addAll(variants);
        }

        Product updated = productRepository.save(product);
        return productMapper.toProductResponse(updated);
    }

    // ==================== DELETE ====================
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


}
