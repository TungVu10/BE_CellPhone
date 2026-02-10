package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueByMonthResponse {
    //Doanh thu theo tháng
    private Integer year;
    private Integer month;
    private BigDecimal revenue;

}
