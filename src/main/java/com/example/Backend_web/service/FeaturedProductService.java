package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.FeaturedProductResponse;
import com.example.Backend_web.dto.response.ProductResponse;

import java.util.List;
import java.util.Map;

public interface FeaturedProductService {
    List<ProductResponse> getFeaturedByType(String type);

    Map<String, List<FeaturedProductResponse>> getAllGrouped();
}

