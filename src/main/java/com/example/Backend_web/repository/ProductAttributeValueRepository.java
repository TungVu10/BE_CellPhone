package com.example.Backend_web.repository;

import com.example.Backend_web.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    // Tìm theo attribute id và value
    Optional<ProductAttributeValue> findByAttributeIdAndValue(Long attributeId, String value);


}

