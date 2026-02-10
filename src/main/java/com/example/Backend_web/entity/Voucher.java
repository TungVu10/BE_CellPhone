package com.example.Backend_web.entity;

import com.example.Backend_web.enums.DiscountType;
import com.example.Backend_web.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voucher {
//    //Id voucher tự generate
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long voucherId;
//    //Mã voucher
//    private String code;
//    // Mô tả Voucher
//    private String description;
//    //Loại giảm giá (% hoặc VNĐ)
//    private String discountType;
//    //Giá trị giảm
//    private BigDecimal discountValue;
//    //Ngày bắt đầu áp dụng Voucher
//    private LocalDateTime startDate;
//    //Ngày kết thúc Voucher
//    private LocalDateTime endDate;
//    //Trạng thái Voucher (hoạt động hoặc tạm dừng)
//    private boolean status;
//
//    //Một Voucher có thể áp dụng cho nhiều sản phẩm
//    @ManyToMany
//    @JoinTable(
//            name = "voucher_product",
//            joinColumns = @JoinColumn(name = "voucher_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id")
//    )
//    private List<Product> products;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;

    @Column(unique = true, nullable = false)
    private String code;
    private String description;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal discountValue;

    private BigDecimal minOrderValue = BigDecimal.ZERO;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

    private Integer quantity;

    private Integer usedCount = 0;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "voucher_product",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


}
