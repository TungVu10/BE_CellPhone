package com.example.Backend_web.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;
    Integer variantId; // Thêm trường này
    String variantName;
    Integer quantity;
    BigDecimal price;
    String image; // 🖼️ thêm trường ảnh
}
