package com.example.Backend_web.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với ProductVariant
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

    // Liên kết với AttributeValue
    @ManyToOne
    @JoinColumn(name = "value_id")
    private ProductAttributeValue attributeValue;

    // Giá trị thực tế người dùng chọn (ví dụ "6GB", "256GB")
    private String value;
}

