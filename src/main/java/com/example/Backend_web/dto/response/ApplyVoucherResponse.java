package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyVoucherResponse {

    private String code;
    private BigDecimal originalPrice;
    //private BigDecimal discountedPrice;
    private BigDecimal discountAmount;   // số tiền được giảm
    private BigDecimal finalPrice;       // giá sau giảm

    private String message;
}

