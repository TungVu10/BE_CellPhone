package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}