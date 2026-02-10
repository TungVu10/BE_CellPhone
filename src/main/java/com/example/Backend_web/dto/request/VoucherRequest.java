package com.example.Backend_web.dto.request;

import com.example.Backend_web.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {
    //productId sản phẩm cần áp dụng Voucher
    //private Integer productId;
//    private Integer variantId;
    //Mã voucher
    private String code;
    // variant đang mua (bắt buộc khi áp voucher)
    private Integer variantId;
    // Mô tả Voucher
    private String description;
    //Loại giảm giá (% hoặc VNĐ)
    //private String discountType;
    private DiscountType discountType;
    //Giá trị giảm
    private BigDecimal discountValue;

    private BigDecimal minOrderValue;

    // Số lượng Voucher
    private Integer quantity;

    private Integer usageLimit;
    //Ngày bắt đầu áp dụng Voucher
    private LocalDateTime startDate;
    //Ngày kết thúc Voucher
    private LocalDateTime endDate;



    // DANH SÁCH productId được áp dụng
    private List<Integer> productIds;
}
