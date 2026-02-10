package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//Entity Sản phẩm HOT
public class HotProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@ManyToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    private LocalDateTime createdAt;
}
