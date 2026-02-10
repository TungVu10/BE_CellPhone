package com.example.Backend_web.repository;

import com.example.Backend_web.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    //Optional<InvoiceItem> findByOrderId(Long orderId);
}
