package com.example.Backend_web.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private String status; // ví dụ: PENDING, CONFIRMED, SHIPPING, COMPLETED, CANCELLED
}
