package com.example.Backend_web.dto.request;

import lombok.Data;

import java.util.List;

// DTO để nhận input từ client
@Data
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private Boolean status;
    private Integer categoryId; // dùng để tìm Category entity
    private List<ProductVariantRequest> variants;
    private List<ProductAttributeValueRequest> attributes; // ✅ thêm dòng này
}

