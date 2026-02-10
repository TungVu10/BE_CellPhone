package com.example.Backend_web.repository;

import com.example.Backend_web.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    // Lấy tất cả ảnh theo productId
    //List<ProductImage> findByProductId(Long productId);

    // Nếu ảnh theo variant
    //List<ProductImage> findByVariant_VariantId(Integer variantId);

    List<ProductImage> findByVariant_Product_ProductId(Integer productId);
}
