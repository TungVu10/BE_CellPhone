package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantImageResponse {
    private Integer id;
    private String imageUrl;
    private Integer variantId;
}

