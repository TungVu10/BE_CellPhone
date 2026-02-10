package com.example.Backend_web.controller;

import com.example.Backend_web.dto.response.VNPayResponse;
import com.example.Backend_web.service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;

//    @PostMapping("/create")
//    public VNPayResponse createPayment(HttpServletRequest request) throws Exception {
//        // Thông tin đơn hàng cơ bản, hardcode hoặc lấy từ session/user
//        long amount = 100000L; // ví dụ 100.000 VND
//        String orderInfo = "Thanh toan don hang 123";
//
//        Map<String, String> params = new HashMap<>();
//        params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu *100
//        params.put("vnp_OrderInfo", orderInfo);
//        params.put("vnp_TxnRef", "123_" + System.currentTimeMillis());
//        params.put("vnp_Locale", "vn");
//        params.put("vnp_OrderType", "other");
//        params.put("vnp_IpAddr", request.getRemoteAddr());
//
//        String paymentUrl = vnPayService.createPaymentUrl(params);
//
//        return new VNPayResponse("00", "success", paymentUrl);
//    }

    //Endpoint tạo hóa đơn thanh toán đơn hàng
    @PostMapping("/create")
    public VNPayResponse createPayment(HttpServletRequest request, @RequestBody Map<String, Object> body) throws Exception {
        long amount = Long.parseLong(body.get("amount").toString()); // tổng tiền thực tế, ví dụ 12_000_000
        String orderInfo = "Thanh toan don hang " + System.currentTimeMillis();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Amount", String.valueOf(amount * 100)); // Nhân 100 lần duy nhất
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_TxnRef", "ORD" + System.currentTimeMillis());
        params.put("vnp_Locale", "vn");
        params.put("vnp_OrderType", "other");
        params.put("vnp_IpAddr", request.getRemoteAddr());

        String paymentUrl = vnPayService.createPaymentUrl(params);
        return new VNPayResponse("00", "success", paymentUrl);
    }


//    @GetMapping("/return")
//    public String vnpayReturn(HttpServletRequest request) {
//        // Lấy tất cả tham số VNPay gửi về, trừ vnp_SecureHash và vnp_SecureHashType
//        Map<String, String> params = new TreeMap<>();
//        request.getParameterMap().forEach((key, value) -> {
//            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
//                params.put(key, value[0]);
//            }
//        });
//
//        String vnpSecureHash = request.getParameter("vnp_SecureHash");
//
//        // Verify chữ ký HMAC SHA512
//        boolean isValid = vnPayService.verifySecureHash(params, vnpSecureHash);
//
//        // Lấy response code
//        String responseCode = request.getParameter("vnp_ResponseCode");
//
//        // Trả về kết quả
//        if (isValid && "00".equals(responseCode)) {
//            return "Thanh toan thanh cong"; // có thể trả JSON nếu muốn
//        } else {
//            return "Thanh toan that bai";
//        }
//    }

    //Endpoint xác thực chữ ký
    @GetMapping("/return")
    public VNPayResponse vnpayReturn(HttpServletRequest request) {
        Map<String, String> params = new TreeMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
                params.put(key, value[0]);
            }
        });

        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        boolean isValid = vnPayService.verifySecureHash(params, vnpSecureHash);
        String responseCode = request.getParameter("vnp_ResponseCode");

        if (isValid && "00".equals(responseCode)) {
            return new VNPayResponse("00", "Thanh toan thanh cong", null);
        } else {
            return new VNPayResponse("01", "Thanh toan that bai", null);
        }
    }

}
