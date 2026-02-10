package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false, length = 200)
    private String name; // Tên sản phẩm chung

    @Column(length = 200)
    private String slug; // SEO URL, có thể tự nhập

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả chung sản phẩm

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // Danh mục

    @Column
    private Boolean status = true; // active/inactive

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants; // Danh sách biến thể

    @ManyToMany(mappedBy = "products")
    private List<Voucher> vouchers;

//    @OneToMany(mappedBy = "product")
//    private List<HotProduct> hotProducts;

}
