package com.example.Backend_web.dto.request;

import lombok.Data;

@Data
public class CheckoutItemRequest {
    private Integer variantId;
    private Integer quantity; //  số lượng MUA
}

