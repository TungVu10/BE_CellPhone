package com.example.Backend_web.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private Integer categoryId;
    private String categoryName;
    private String categorySlug;

    private Integer parentId; // chỉ lưu id của danh mục cha
}
