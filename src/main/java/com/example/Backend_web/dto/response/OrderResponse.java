package com.example.Backend_web.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String customerName;
    String customerPhone;
    String customerEmail;
    String customerAddress;
    //Phương thức thanh toán của khách hàng
    String customerPaymentMethod;
    //Phương thức vận chuyển khách hàng chọn
    //String customerShippingMethod;

    String status;
    private boolean hasInvoice; // Check xem đã có hóa đơn chưa
    LocalDateTime orderDate;
    BigDecimal totalPrice;
    List<OrderItemResponse> items;
}