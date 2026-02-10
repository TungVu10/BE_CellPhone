package com.example.Backend_web.repository;

import com.example.Backend_web.entity.FavoriteProduct;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    boolean existsByUserAndProduct( User user, Product product);

    List<FavoriteProduct> findByUser_Id(Long userId);

    Optional<FavoriteProduct> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);
}
