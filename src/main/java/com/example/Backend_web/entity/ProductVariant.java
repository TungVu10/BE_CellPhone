package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Sản phẩm gốc

    @Column(nullable = false, length = 50)
    private String name; // Ví dụ: "256GB Đen", "512GB Trắng"

    @Column(nullable = false, precision = 38, scale = 2)
    private BigDecimal price;

    @Column
    private Integer quantity;

    @Column
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;


    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images; // Hình ảnh riêng cho variant

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductVariantAttribute> variantAttributes;



    // Update thông tin sản phẩm
//    @OneToMany(mappedBy = "variant")
//    private List<OrderItem> orderItems;


}
