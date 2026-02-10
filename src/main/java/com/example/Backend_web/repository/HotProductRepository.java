package com.example.Backend_web.repository;

import com.example.Backend_web.entity.HotProduct;
import com.example.Backend_web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotProductRepository extends JpaRepository<HotProduct, Long> {
    @Query("""
    SELECT h.product
    FROM HotProduct h
""")
    List<Product> findHotProducts();

    boolean existsByProduct(Product id);

    Optional<HotProduct> findByProduct(Product product);

}
