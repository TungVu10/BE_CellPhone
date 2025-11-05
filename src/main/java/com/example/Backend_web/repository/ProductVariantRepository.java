package com.example.Backend_web.repository;

import com.example.Backend_web.entity.ProductVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    // Tìm các variant theo productId
    List<ProductVariant> findByProduct_ProductId(Integer productId);

    // Tìm variant theo tên (VD: "256GB Đen")
    List<ProductVariant> findByNameContainingIgnoreCase(String name);

//    @Query("""
//    SELECT DISTINCT v FROM ProductVariant v
//    LEFT JOIN FETCH v.images
//    LEFT JOIN FETCH v.product p
//    LEFT JOIN FETCH p.variants pv
//    LEFT JOIN FETCH pv.images
//    WHERE v.variantId = :id
//""")
//    Optional<ProductVariant> findVariantWithImages(@Param("id") Integer id);

    @Query("""
    SELECT DISTINCT v FROM ProductVariant v
    LEFT JOIN FETCH v.images
    WHERE v.variantId = :id
""")
    Optional<ProductVariant> findVariantWithImages(@Param("id") Integer id);


}
