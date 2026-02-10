package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {

    private Long id;
    private String invoiceCode;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    //Phương thức thanh toán của khách hàng
    private String customerPaymentMethod;
    //Phương thức vận chuyển khách hàng chọn
    //private String customerShippingMethod;

    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    private List<InvoiceItemResponse> items;
}
