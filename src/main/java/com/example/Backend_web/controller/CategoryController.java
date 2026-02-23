package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.CategoryRequest;
import com.example.Backend_web.dto.response.AttributeResponse;
import com.example.Backend_web.dto.response.CategoryResponse;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.Category;
import com.example.Backend_web.entity.ProductAttribute;
import com.example.Backend_web.service.CategoryService;
import com.example.Backend_web.service.ProductAttributeService;
import com.example.Backend_web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductAttributeService productAttributeService;
    private final ProductService productService;

    // Thêm category mới
//    @PostMapping("/add")
//    public ResponseEntity<CategoryResponse> addCategory(@RequestBody Category category) {
//        CategoryResponse response = categoryService.addCategory(category);
//        return ResponseEntity.ok(response);
//    }

    // Thêm category mới
    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.addCategory(request);
        return ResponseEntity.ok(response);
    }



    // Lấy tất cả category
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> list = categoryService.getAllCategories();
        return ResponseEntity.ok(list);
    }

    // Lấy category theo id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    // Lấy danh mục cha
    @GetMapping("/parents")
    public ResponseEntity<List<CategoryResponse>> getParentCategories() {
        List<CategoryResponse> parents = categoryService.getParentCategories();
        return ResponseEntity.ok(parents);
    }

    // Lấy danh mục con theo parentId
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryResponse>> getChildCategories(@PathVariable Integer parentId) {
        List<CategoryResponse> children = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(children);
    }

    // Lấy category theo slug (dùng cho URL /mobile/apple)
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse response = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(response);
    }

    // API: GET /categories/{id}/attributes
    @GetMapping("/{id}/attributes")
    public ResponseEntity<List<AttributeResponse>> getAttributesByCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(productAttributeService.getAttributesByCategory(id));
    }

    //API Update danh mục sản phẩm
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest request){
        return ResponseEntity.ok(categoryService.updateCategory(id,request));
    }

    //API Xóa danh mục sản phẩm
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<ProductResponse> getByCategory(@PathVariable Integer categoryId) {
        Set<Integer> categoryIds = categoryService.getAllChildCategoryIds(categoryId);
        // 🔥 thêm chính nó vào
        categoryIds.add(categoryId);
        return productService.getProductsByCategoryIds(categoryIds);
    }

    // Lấy tat cả các Sản phẩm của danh mục con như Apple, SamSung,...
//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
//        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
//    }

}
