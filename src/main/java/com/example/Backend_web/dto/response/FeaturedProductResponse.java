package com.example.Backend_web.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedProductResponse {
    private Long id;
    private Integer productId;
    private String productName;
    private String type;
    private Integer priority;
}

