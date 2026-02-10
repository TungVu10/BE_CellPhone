package com.example.Backend_web.repository;

import com.example.Backend_web.dto.response.RevenueByMonthResponse;
import com.example.Backend_web.entity.Order;
import com.example.Backend_web.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    List<Order> findByUser_Username(String username);

    Optional<Order> findByIdAndUser_Username(Long id, String username);

    @Query("""
        SELECT o FROM Order o 
        WHERE (:status IS NULL OR o.status = :status)
        AND (:keyword IS NULL 
             OR o.receiverName LIKE %:keyword%
             OR o.receiverPhone LIKE %:keyword%
             OR o.receiverAddress LIKE %:keyword%)
    """)
    Page<Order> searchOrders(
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    //Thống kê tổng số đơn hàng
    @Query("SELECT COUNT(o) from Order o ")
    long countOrders();

    //Tổng doanh thu đơn hàng (với đơn hàng đã được giao thành công và hoàn thành đơn hàng Complete)
    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.status = 'COMPLETED'
""")
    BigDecimal sumRevenue();

    //Tổng số đơn đang ở trạng thái chờ giao hàng (trạng thái PENDING)
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.status = 'PENDING'
""")
    long countPendingOrders();

    //Doanh thu theo tháng (chỉ tính với nhưng đơn hàng đã hoàn thành ở trạng thái COMPLETED)
    @Query("""
        SELECT new com.example.Backend_web.dto.response.RevenueByMonthResponse(
            YEAR(o.orderDate),
            MONTH(o.orderDate),
            SUM(o.totalPrice)
        )
        FROM Order o
        WHERE o.status = :status
        GROUP BY YEAR(o.orderDate), MONTH(o.orderDate)
        ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)
    """)
    List<RevenueByMonthResponse> revenueByMonth(OrderStatus status);

    // Đếm số lần từng USER đã dùng Voucher đó
    @Query("""
   SELECT COUNT(o)
   FROM Order o
   WHERE o.voucher.voucherId = :voucherId
     AND o.user.id = :userId
     AND o.status IN (
         com.example.Backend_web.enums.OrderStatus.PAID,
         com.example.Backend_web.enums.OrderStatus.COMPLETED
     )
""")
    long countVoucherUsedByUser(Long voucherId, Long userId);



}