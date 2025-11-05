package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: RAM, Màu sắc, Ổ cứng

    // 1 Attribute thuộc 1 Category (Laptop, Mobile,...)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 1 Attribute có nhiều giá trị
    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<ProductAttributeValue> values;
}
