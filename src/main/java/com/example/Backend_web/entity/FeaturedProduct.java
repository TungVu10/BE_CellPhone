package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "featured_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeaturedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Loại nổi bật: BEST_SELLER, HOT, TRENDING, DEAL, NEW_ARRIVAL,...
    @Column(nullable = false, length = 50)
    private String type;

    // Thứ tự hiển thị (ưu tiên)
    @Column(nullable = false)
    private Integer priority = 0;
}

