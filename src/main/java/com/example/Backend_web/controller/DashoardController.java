package com.example.Backend_web.controller;

import com.example.Backend_web.dto.response.DashboardSummaryResponse;
import com.example.Backend_web.dto.response.RevenueByMonthResponse;
import com.example.Backend_web.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashoardController {
    private final DashboardService dashboardService;

    //Api Thống kê tổng số đơn hàng
    @GetMapping("/summary")
    public DashboardSummaryResponse summary() {
        return dashboardService.getSummary();
    }

    //Api Thống kê doanh thu theo tháng
    @GetMapping("/revenue")
    public List<RevenueByMonthResponse> revenue() {
        return dashboardService.getRevenueByMonth();
    }
}
