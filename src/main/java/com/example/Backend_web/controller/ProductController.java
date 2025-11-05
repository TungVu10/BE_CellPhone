package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.ProductRequest;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //Thêm product
    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.addProduct(request);
        return ResponseEntity.ok(response);
    }

    // Sửa product
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // Xóa product
    @DeleteMapping("/delete/{id}")
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




}
