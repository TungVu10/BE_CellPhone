package com.example.Backend_web.controller;

import com.example.Backend_web.service.HotProductService;
import com.example.Backend_web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hot-products")
@RequiredArgsConstructor
public class HotProductController {
    private final ProductService productService;
    private final HotProductService hotProductService;

    //Endpoint lấy danh sách các Sản phẩm HOT
    @GetMapping
    public ResponseEntity<?> getHotProducts() {
        return ResponseEntity.ok(hotProductService.getHotProducts());
    }

    //Endpoint Set Sản phẩm HOT
    @PostMapping("/{productId}")
    public ResponseEntity<?> SetHot(@PathVariable Integer productId){
        hotProductService.setHotProduct(productId);
        return ResponseEntity.ok("San pham nay la San Pham HOT");
    }

    //EndPoint Xóa Sản phẩm HOT
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHotProduct(@PathVariable Integer productId){
        //productService.deleteProduct(productId);
        hotProductService.deleteHotProduct(productId);
        return ResponseEntity.ok("Xoa San pham HOT thanh cong");
    }
}
