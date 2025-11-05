package com.example.Backend_web.repository;

import com.example.Backend_web.entity.FeaturedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeaturedProductRepository extends JpaRepository<FeaturedProduct, Long> {
    List<FeaturedProduct> findByTypeOrderByPriorityAsc(String type);
}
