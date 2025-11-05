package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.CategoryResponse;
import com.example.Backend_web.entity.Category;
import com.example.Backend_web.mapper.CategoryMapper;
import com.example.Backend_web.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Thêm category mới
    public CategoryResponse addCategory(Category category) {
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            throw new IllegalArgumentException("Slug không được để trống. Vui lòng nhập slug trong JSON.");
        }
        Category saved = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(saved);
    }

    // Lấy tất cả category
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    // Lấy category theo id
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toCategoryResponse(category);
    }

    // Lấy danh mục cha (parent = null)
    public List<CategoryResponse> getParentCategories() {
        return categoryRepository.findByParentIsNull()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    // Lấy danh mục con theo parentId
    public List<CategoryResponse> getChildCategories(Integer parentId) {
        return categoryRepository.findByParent_CategoryId(parentId)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    // Lấy category theo slug
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toCategoryResponse(category);
    }
}
