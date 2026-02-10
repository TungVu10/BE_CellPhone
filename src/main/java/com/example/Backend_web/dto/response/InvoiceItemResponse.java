package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemResponse {

    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}

