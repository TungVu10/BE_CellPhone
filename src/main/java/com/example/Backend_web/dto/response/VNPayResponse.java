package com.example.Backend_web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNPayResponse {
    private String code;        // ví dụ "00"
    private String message;     // ví dụ "success"
    private String paymentUrl;  // link VNPay
}

