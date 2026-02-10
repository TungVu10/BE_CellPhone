package com.example.Backend_web.dto.request;

import lombok.Data;

@Data
public class UpdateCartQuantityRequest {
    private Long userId;
    private Integer variantId;
    private Integer quantity;
}
