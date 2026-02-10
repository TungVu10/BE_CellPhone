package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.ProductRequest;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.dto.response.ProductSearchResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //Thêm product
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.addProduct(request);
        return ResponseEntity.ok(response);
    }

    // Sửa product
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // Xóa product
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // trả về 204 No Content
    }

    //Lấy tất cả products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //Lấy product theo id
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ProductController.java
    @GetMapping("/category/{slug}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String slug) {
        List<ProductResponse> list = productService.getProductsByCategorySlug(slug);
        return ResponseEntity.ok(list);
    }

    // Lấy chi tiết sản phẩm theo slug
//    @GetMapping("/detail/{slug}")
//    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable String slug) {
//        return ResponseEntity.ok(productService.getProductDetailBySlug(slug));
//    }

//    @GetMapping("/{slug}")
//    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
//        ProductResponse product = productService.getProductBySlug(slug);
//        return ResponseEntity.ok(product);
//    }


    // ==================== SMART SEARCH ====================

    /**
     * 🔎 Search thông minh giống CellphoneS:
     * - full-text search (name, description)
     * - sắp xếp theo độ liên quan
     * - trả về ảnh & giá variant đầu tiên
     *
     * Ví dụ API:
     * /api/products/search?keyword=iphone&page=0&size=10
     */
    // -------- SMART SEARCH (CellphoneS style) ----------
    @GetMapping("/search/suggest")
    public ResponseEntity<List<ProductSearchResponse>> searchSmart(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.searchSmart(keyword, page, size));
    }

    // EndPoint AUTOCOMPLETE
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(productService.autocomplete(keyword));
    }

//    // EndPoint Tìm Sản phẩm theo các tiêu chí
//    @GetMapping("/search/price-desc")
//    public ResponseEntity<List<ProductResponse>> searchPriceDesc(
//            @RequestParam Integer categoryId,
//            @RequestParam(defaultValue = "") String keyword
//    ) {
//        return ResponseEntity.ok(
//                productService.searchOrderByPriceDesc(categoryId, keyword)
//        );
//    }
//
//    @GetMapping("/search/price-asc")
//    public ResponseEntity<List<ProductResponse>> searchPriceAsc(
//            @RequestParam Integer categoryId,
//            @RequestParam(defaultValue = "") String keyword
//    ) {
//        return ResponseEntity.ok(
//                productService.searchOrderByPriceAsc(categoryId, keyword)
//        );
//    }

    //  Search + Sort giá
    // EndPoint Lọc Sản phẩm theo tiêu chí Giá giảm dần, tăng dần
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "price_asc") String sort
    ) {
        return ResponseEntity.ok(
                productService.searchProducts(categoryId, keyword, sort)
        );
    }

    // EndPoint Lọc Sản phẩm theo tiêu chí Dung lượng RAM, ROM
    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> filterByAttribute(
            @RequestParam String code,
            @RequestParam String value
    ){
        return ResponseEntity.ok(productService.filterByAttribute(code, value));
    }

    // EndPoint Hiển thị danh sách Sản phẩm liên quan
    @GetMapping("/{id}/related")
    public ResponseEntity<?> getRelated(@PathVariable Integer id){
        return ResponseEntity.ok(
                productService.getRelatedProducts(id)
        );
    }


}
