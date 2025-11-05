package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId; // Khóa chính

    @Column(name = "name", nullable = false, length = 150)
    private String name; // Tên danh mục (Điện thoại, Laptop, ...)

    @Column(name = "slug", unique = true, length = 200)
    private String slug; // Dùng cho SEO (vi-du: dien-thoai, laptop)

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Mô tả thêm (nếu cần)

//    @Column(name = "image")
//    private String image; // Ảnh đại diện danh mục (banner, icon)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; // Danh mục cha (null nếu là gốc)

    @Column(name = "status")
    private Boolean status = true; // Trạng thái (active/inactive)
}
