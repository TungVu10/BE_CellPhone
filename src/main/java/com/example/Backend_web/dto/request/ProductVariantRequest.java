package com.example.Backend_web.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductVariantRequest {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Integer colorId; // dùng để tìm Color entity
    private List<String> images; // list URL
    // ✅ Thêm thuộc tính riêng cho từng variant
    private List<ProductAttributeValueRequest> attributes;
}
