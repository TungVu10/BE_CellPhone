package com.example.Backend_web.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer productId;
    private String name;
    private String slug;
    private String description;
    private Boolean status;
    private Boolean isHot;
    private Integer categoryId;
    private Integer parentCategoryId;  // ✅ THÊM TRƯỜNG NÀY
    private List<ProductVariantResponse> variants;
}
