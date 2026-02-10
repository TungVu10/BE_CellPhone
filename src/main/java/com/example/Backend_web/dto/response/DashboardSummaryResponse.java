package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryResponse {
    //Tổng số đơn hàng
    private long totalOrders;
    //Tổng số người dùng
    private long totalUsers;
    //Tổng số đơn hàng đang trong trạng thái chờ (PENDING)
    private long pendingOrders;
    //Tổng doanh thu
    private BigDecimal totalRevenue;
}
