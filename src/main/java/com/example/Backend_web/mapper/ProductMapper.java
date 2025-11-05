package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.response.AttributeValueResponse;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.dto.response.ProductVariantResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors; // ✅ thêm cái này
import java.util.List;

@Mapper(componentModel = "spring", imports = {Collectors.class, List.class})
public interface ProductMapper {

    @Mapping(target = "categoryId", expression = "java(product.getCategory() != null ? product.getCategory().getCategoryId() : null)")

    @Mapping(target = "parentCategoryId",
            expression = "java(product.getCategory() != null && product.getCategory().getParent() != null ? product.getCategory().getParent().getCategoryId() : null)")

    @Mapping(target = "variants", expression = "java(product.getVariants() != null ? product.getVariants().stream().map(this::toVariantResponse).collect(Collectors.toList()) : null)")
    ProductResponse toProductResponse(Product product);

    default ProductVariantResponse toVariantResponse(ProductVariant variant) {
        return new ProductVariantResponse(
                variant.getVariantId(),
                variant.getName(),
                variant.getPrice(),
                variant.getQuantity(),
                variant.getStatus(),
                variant.getColor() != null ? variant.getColor().getColorId() : null, // ✅ colorId
                variant.getColor() != null ? variant.getColor().getName() : null,
                variant.getImages() != null
                        ? variant.getImages().stream().map(i -> i.getImageUrl()).collect(Collectors.toList())
                        : null,
//                variant.getVariantAttributes() != null
//                        ? variant.getVariantAttributes().stream()
//                        .map(a -> new AttributeValueResponse(
//                                a.getAttributeValue().getId(),
//                                a.getAttributeValue().getValue()
//                        )).collect(Collectors.toList())
//                        : null

//                variant.getVariantAttributes() != null
//                        ? variant.getVariantAttributes().stream()
//                        .map(a -> new AttributeValueResponse(
//                                a.getAttributeValue().getAttribute().getId(), // ID của attribute (RAM, ROM…)
//                                a.getValue()                                  // giá trị thực sự người dùng chọn
//                        )).collect(Collectors.toList())
//                        : null

                variant.getVariantAttributes() != null
                        ? variant.getVariantAttributes().stream()
                        .map(a -> new AttributeValueResponse(
                                a.getAttributeValue().getId(),   // ✅ ID attribute (RAM, ROM, Pin, ...)
                                //a.getAttributeValue().getValue()                 // ✅ Giá trị của attribute (8GB, 256GB, ...)
                                a.getValue()
                        ))
                        .collect(Collectors.toList())
                        : null


        );
    }




}
