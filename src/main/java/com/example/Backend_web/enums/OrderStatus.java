package com.example.Backend_web.enums;

public enum OrderStatus {

    PENDING,        // Đã tạo đơn (chưa thanh toán)
    PAID,           // Đã thanh toán
    CONFIRMED,      // Admin xác nhận đơn
    SHIPPING,       // Đang giao hàng
    DELIVERED,      // Đã giao thành công
    COMPLETED,      // Hoàn tất (sau khi đối soát)
    CANCELLED       // Đã hủy
}

