package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Tìm sản phẩm theo tên (like search)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Tìm sản phẩm theo category
    List<Product> findByCategoryCategoryId(Integer categoryId);

    // Lấy tất cả sản phẩm theo slug của danh mục
    @Query("SELECT p FROM Product p WHERE p.category.slug = :slug")
    List<Product> findAllByCategorySlug(@Param("slug") String slug);

    // Tìm sản phẩm theo category + khoảng giá
    @Query("SELECT p FROM Product p JOIN p.variants v " +
            "WHERE p.category.categoryId = :categoryId " +
            "AND v.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndPriceRange(@Param("categoryId") Integer categoryId,
                                              @Param("minPrice") BigDecimal minPrice,
                                              @Param("maxPrice") BigDecimal maxPrice);

    // Tìm sản phẩm theo màu
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.variants v " +
            "WHERE v.color.colorId = :colorId")
    List<Product> findByColor(@Param("colorId") Integer colorId);

    // Tìm sản phẩm theo category + màu + khoảng giá
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.variants v " +
            "WHERE p.category.categoryId = :categoryId " +
            "AND v.color.colorId = :colorId " +
            "AND v.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndColorAndPriceRange(@Param("categoryId") Integer categoryId,
                                                      @Param("colorId") Integer colorId,
                                                      @Param("minPrice") BigDecimal minPrice,
                                                      @Param("maxPrice") BigDecimal maxPrice);

    // Tìm sản phẩm theo slug
    Optional<Product> findBySlug(String slug);
}
