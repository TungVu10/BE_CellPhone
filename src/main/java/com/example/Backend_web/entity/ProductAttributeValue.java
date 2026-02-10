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
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value; // Ví dụ: 8GB, 16GB, Đen, Bạc, 512GB SSD

    @ManyToOne
    //@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    // Liên kết nhiều giá trị với nhiều ProductVariant qua bảng trung gian
    @OneToMany(mappedBy = "attributeValue", cascade = CascadeType.ALL)
    private List<ProductVariantAttribute> variantAttributes;
}

