package com.example.Backend_web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethod {
    @Id
    @GeneratedValue
    private long id;
    //Tên phương thức vận chuyển
    private String name;
    //Mô tả phương thức vận chuyển
    private String description;
    //Thời gian giao hàng
    private Integer estimateDays;
    private boolean status;
}
