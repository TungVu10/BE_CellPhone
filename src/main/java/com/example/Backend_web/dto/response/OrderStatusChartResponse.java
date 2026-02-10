package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChartResponse {
    //Trạng thái đơn hàng
    private String status;
    //Số lượng đơn hàng theo từng loại trạng thái
    private long count;
}
