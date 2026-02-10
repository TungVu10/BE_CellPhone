package com.example.Backend_web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyVoucherRequest {

    private String code;

    private Long userId;

    private BigDecimal orderTotal;

    // optional – nếu muốn check theo variant
    private Integer variantId;
}

