package com.example.Backend_web.repository;

import com.example.Backend_web.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);
}

