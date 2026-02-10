package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.DashboardSummaryResponse;
import com.example.Backend_web.dto.response.RevenueByMonthResponse;
import com.example.Backend_web.enums.OrderStatus;
import com.example.Backend_web.repository.OrderRepository;
import com.example.Backend_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    //Tính tổng:
    //Đếm số lượng đơn
    //Tổng số khách hàng
    //Đếm tong số đơn hàng trạng thái chờ giao hàng (PENDING)
    //Tính tổng doanh thu 
    public DashboardSummaryResponse getSummary(){
        return new DashboardSummaryResponse(
                orderRepository.countOrders(),
                userRepository.countUsers(),
                orderRepository.countPendingOrders(),
                orderRepository.sumRevenue()
        );
    }

    //Tính tổng doanh thu theo tháng
    public List<RevenueByMonthResponse> getRevenueByMonth() {
        return orderRepository.revenueByMonth(OrderStatus.COMPLETED);
    }

}
