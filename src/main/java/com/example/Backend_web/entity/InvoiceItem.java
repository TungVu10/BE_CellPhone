package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Thông tin sản phẩm
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
