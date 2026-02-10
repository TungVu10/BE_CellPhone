package com.example.Backend_web.service;

import com.example.Backend_web.entity.VNPayConfig;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
public class VNPayService {

    private final VNPayConfig config;

    public VNPayService(VNPayConfig config) {
        this.config = config;
    }

    public String createPaymentUrl(Map<String, String> orderParams) throws Exception {
        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
        vnp_Params.putAll(orderParams);
        vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());

        vnp_Params.put("vnp_CurrCode", "VND"); // luôn có
        vnp_Params.putIfAbsent("vnp_Locale", "vn");
        vnp_Params.putIfAbsent("vnp_OrderType", "other");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", now.format(fmt));
        vnp_Params.put("vnp_ExpireDate", now.plusMinutes(15).format(fmt));

        TreeMap<String, String> sorted = new TreeMap<>(vnp_Params); // sort A->Z
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                hashData.append(entry.getKey()).append('=').append(value).append('&');

                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append('=')
                        .append(value)
                        .append('&');
            }
        }
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String vnpSecureHash = hmacSHA512(config.getHashSecret(), hashData.toString());
        return config.getPayUrl() + "?" + query + "&vnp_SecureHash=" + vnpSecureHash;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) hash.append(String.format("%02x", b));
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Xác thực chữ ký
    public boolean verifySecureHash(Map<String, String> params, String vnpSecureHash) {
        TreeMap<String, String> sorted = new TreeMap<>(params); // sort A->Z
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                hashData.append(entry.getKey()).append('=').append(value).append('&');
            }
        }
        hashData.setLength(hashData.length() - 1);
        String myHash = hmacSHA512(config.getHashSecret(), hashData.toString());
        return myHash.equalsIgnoreCase(vnpSecureHash);
    }

}
