package com.example.Backend_web.repository;

import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.Product;
import org.springframework.data.domain.Pageable;
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

    // Tìm Sản phẩm theo Category + Tên + Giá tăng dần
    List<Product> findByCategory_CategoryIdAndNameContainingIgnoreCaseOrderByVariants_PriceAsc(
            Integer categoryId,
            String name
    );

    // Tìm Sản phẩm theo Category + Tên + Giá giảm dần

    // Query Giá Sản phẩm Giảm dần
    @Query("""
SELECT p
FROM Product p
LEFT JOIN p.variants v
WHERE p.category.categoryId = :categoryId
  AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
GROUP BY p
ORDER BY MAX(v.price) DESC
""")
    List<Product> orderByPriceDesc(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword
    );



    // Query Giá Sản phẩm Tăng dần
    @Query("""
SELECT p
FROM Product p
LEFT JOIN p.variants v
WHERE p.category.categoryId = :categoryId
  AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
GROUP BY p
ORDER BY MIN(v.price) ASC
""")
    List<Product> orderByPriceAsc(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword
    );

    // Query Dung lượng RAM Sản phẩm
    @Query("""
SELECT DISTINCT p
FROM Product p
JOIN p.variants v
JOIN v.variantAttributes pva
JOIN pva.attributeValue pav
JOIN pav.attribute a
WHERE a.name = :code
  AND pva.value = :value
""")
    List<Product> filterByAttribute(
            @Param("code") String code,
            @Param("value") String value
    );








    // Tìm sản phẩm theo slug
    Optional<Product> findBySlug(String slug);

    @Query(value = """
        SELECT * FROM products p
        WHERE 
            -- 1) Tên bắt đầu bằng keyword đầy đủ
            LOWER(p.name) LIKE CONCAT(:keyword, '%')

            -- 2) AND logic: chứa toàn bộ các từ khóa
            OR (
                LOWER(p.name) LIKE :andPattern
                AND (
                    SELECT COUNT(*)
                    FROM (
                        SELECT TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(:andPattern, ' ', n.n), ' ', -1)) AS term
                        FROM (
                            SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
                        ) n
                    ) AS terms
                    WHERE term <> '' AND LOWER(p.name) LIKE CONCAT('%', term, '%')
                ) = :termCount
            )

        ORDER BY 
            CASE 
                WHEN LOWER(p.name) LIKE CONCAT(:keyword, '%') THEN 1
                ELSE 2
            END,
            p.name ASC
        
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true)
    List<Product> searchSmart(
            @Param("keyword") String keyword,
            @Param("andPattern") String andPattern,
            @Param("termCount") int termCount,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    // AUTOCOMPLETE giữ nguyên
    @Query(value = """
        SELECT name FROM products
        WHERE LOWER(name) LIKE LOWER(CONCAT(:keyword, '%'))
        ORDER BY name ASC
        LIMIT 10
        """,
            nativeQuery = true)
    List<String> autocomplete(@Param("keyword") String keyword);



    // Tìm sản phẩm theo category + khoảng giá
//    @Query("SELECT p FROM Product p JOIN p.variants v " +
//            "WHERE p.category.categoryId = :categoryId " +
//            "AND v.price BETWEEN :minPrice AND :maxPrice")
//    List<Product> findByCategoryAndPriceRange(@Param("categoryId") Integer categoryId,
//                                              @Param("minPrice") BigDecimal minPrice,
//                                              @Param("maxPrice") BigDecimal maxPrice);
//
//    // Tìm sản phẩm theo màu
//    @Query("SELECT DISTINCT p FROM Product p " +
//            "JOIN p.variants v " +
//            "WHERE v.color.colorId = :colorId")
//    List<Product> findByColor(@Param("colorId") Integer colorId);
//
//    // Tìm sản phẩm theo category + màu + khoảng giá
//    @Query("SELECT DISTINCT p FROM Product p " +
//            "JOIN p.variants v " +
//            "WHERE p.category.categoryId = :categoryId " +
//            "AND v.color.colorId = :colorId " +
//            "AND v.price BETWEEN :minPrice AND :maxPrice")
//    List<Product> findByCategoryAndColorAndPriceRange(@Param("categoryId") Integer categoryId,
//                                                      @Param("colorId") Integer colorId,
//                                                      @Param("minPrice") BigDecimal minPrice,
//                                                      @Param("maxPrice") BigDecimal maxPrice);


    // Query hiển thị danh sách Sản phẩm liên quan
    @Query("""
        SELECT p FROM Product p 
        WHERE p.category.categoryId = :categoryId
        AND p.productId <> :currentId
        
""")
    List<Product> findRelatedByCategory(
            @Param("categoryId") Integer categoryId,
            @Param("currentId") Integer currentId
    );
}
