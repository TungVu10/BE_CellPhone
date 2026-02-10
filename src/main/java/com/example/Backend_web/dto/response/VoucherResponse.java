package com.example.Backend_web.dto.response;

import com.example.Backend_web.enums.DiscountType;
import com.example.Backend_web.enums.VoucherStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {
//    //Mã Voucher
//    private String codeVoucher;
//    //Giá gốc
//    private BigDecimal originalPrice;
//    //Giá sau khi giảm (sau khi áp dụng voucher)
//    private BigDecimal discountedPrice;
//    //Thông báo (Voucher áp dụng thành công / Voucher hết hạn)
//    private String message;

    private Long id;
    private String code;
    private String description;

    private DiscountType discountType;
    private BigDecimal discountValue;

    private BigDecimal minOrderValue;

    private Integer quantity;
    private Integer usedCount;
    private Integer usageLimit;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private VoucherStatus status;
}
