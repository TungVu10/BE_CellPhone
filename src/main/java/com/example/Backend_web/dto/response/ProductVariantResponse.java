package com.example.Backend_web.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponse {
    private Integer variantId;
    private String name; // Ví dụ: "128GB", "256GB"
    private BigDecimal price;
    private Integer quantity;
    private Boolean status;
    private Integer colorId; // ✅ Thêm
    private String colorName; // tên màu
    private List<String> images; // URL ảnh
    private List<AttributeValueResponse> attributes; // ✅ thêm thuộc tính
}
