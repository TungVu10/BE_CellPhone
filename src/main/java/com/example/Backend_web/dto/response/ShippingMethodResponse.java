package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingMethodResponse {
    //Tên phương thức vận chuyển
    private String name;
    //Mô tả phương thức vận chuyển
    private String description;
    //Thời gian giao hàng
    private Integer estimateDays;
}
