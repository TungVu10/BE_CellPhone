package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeValueResponse {
    private Long id;
    private String value;
}
