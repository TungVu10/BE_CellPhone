package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.AttributeResponse;
import com.example.Backend_web.dto.response.AttributeValueResponse;
import com.example.Backend_web.entity.Category;
import com.example.Backend_web.entity.ProductAttribute;
import com.example.Backend_web.repository.CategoryRepository;
import com.example.Backend_web.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;

    public List<AttributeResponse> getAttributesByCategory(Integer categoryId) {
        // 1️ Lấy attribute của category con
        List<ProductAttribute> attributes = productAttributeRepository.findByCategory_CategoryId(categoryId);

        // 2 Nếu category con chưa có attribute, lấy từ parent
        if (attributes == null || attributes.isEmpty()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null && category.getParent() != null) {
                attributes = productAttributeRepository.findByCategory_CategoryId(category.getParent().getCategoryId());
            }
        }

        // 3️ Map sang DTO trả về
        return attributes.stream()
                .map(attribute -> new AttributeResponse(
                        attribute.getId(),
                        attribute.getName(),
                        attribute.getValues().stream()
                                .map(v -> new AttributeValueResponse(v.getId(), v.getValue()))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
    }
}
