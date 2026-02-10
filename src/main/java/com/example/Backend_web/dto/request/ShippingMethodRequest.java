package com.example.Backend_web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodRequest {
    //Tên phương thức vận chuyển
    private String name;
    //Mô tả phương thức vận chuyển
    private String description;
    //Thời gian giao hàng
    private Integer estimateDays;
}
