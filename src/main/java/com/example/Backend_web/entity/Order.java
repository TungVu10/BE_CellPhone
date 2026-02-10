package com.example.Backend_web.entity;

import com.example.Backend_web.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    //private String status; // PENDING, PAID, SHIPPED, COMPLETED
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // Thêm vào:
    // Yeu cầu khách hàng nhập thông tin để thanh toán đơn hàng
    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverAddress;
    //Phương thức thanh toán
    private String paymentMethod;
    //Phương thức vận chuyển
    @ManyToOne
    @JoinColumn(name = "shipping_method_id")
    private ShippingMethod shippingMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    //Voucher
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    private BigDecimal discountAmount;   // so tien duoc giam
}
