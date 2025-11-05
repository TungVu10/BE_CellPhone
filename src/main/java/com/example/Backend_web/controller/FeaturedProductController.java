package com.example.Backend_web.controller;

import com.example.Backend_web.dto.response.FeaturedProductResponse;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.service.FeaturedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/featured-products")
@RequiredArgsConstructor
public class FeaturedProductController {

    private final FeaturedProductService service;

    @GetMapping("/{type}")
    public List<ProductResponse> getFeatured(@PathVariable String type) {
        return service.getFeaturedByType(type);
    }

    @GetMapping("/grouped")
    public Map<String, List<FeaturedProductResponse>> getAllGrouped() {
        return service.getAllGrouped(); // (nếu bạn đã thêm theo hướng dẫn trước đó)
    }
}

