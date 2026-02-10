package com.example.Backend_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
//Bảng hóa đơn
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Mã hóa đơn
    private String invoiceCode; // HD20251225001

    //Thông tin Người mua hàng (Khách hàng)
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;

    private String customerPaymentMethod;

    //Tổng tiền
    private BigDecimal totalAmount;

    //Ngày khởi tạo hóa đơn
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
}
