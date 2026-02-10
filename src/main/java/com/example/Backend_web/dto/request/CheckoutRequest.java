package com.example.Backend_web.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private Long userId;
    private List<Integer> variantIds; // Nếu null hoặc trống → checkout toàn bộ giỏ hàng

    //  THÊM CHO BUY NOW
    private Boolean buyNow;

    private List<CheckoutItemRequest> items;

    // Thông tin nhận hàng
    private String fullName;
    private String phone;
    private String address;

    private String email;

    // Phương thức thanh toán
    private String paymentMethod; // COD, VNPAY, MOMO,...
    //Phương thức vận chuyển
    //private Long shippingMethodId;

    // Mã Voucher
    private String voucherCode;
}
