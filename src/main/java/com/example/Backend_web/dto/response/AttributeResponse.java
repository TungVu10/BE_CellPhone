package com.example.Backend_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AttributeResponse {
    private Long id;
    private String name;
    private List<AttributeValueResponse> values;
}
