package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Cart;
import com.example.Backend_web.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}
