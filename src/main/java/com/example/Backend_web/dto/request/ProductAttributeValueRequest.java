package com.example.Backend_web.dto.request;

import lombok.Data;

@Data
public class ProductAttributeValueRequest {
    private Long attributeId;  // ✅ sửa từ Integer sang Long
    //private Long Id;
    private String value;
}
