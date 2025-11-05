package com.example.Backend_web.service.impl;

import com.example.Backend_web.dto.response.FeaturedProductResponse;
import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.mapper.ProductMapper;
import com.example.Backend_web.repository.FeaturedProductRepository;
import com.example.Backend_web.service.FeaturedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeaturedProductServiceImpl implements FeaturedProductService {

    private final FeaturedProductRepository repository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getFeaturedByType(String type) {
        return repository.findByTypeOrderByPriorityAsc(type)
                .stream()
                .map(fp -> productMapper.toProductResponse(fp.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<FeaturedProductResponse>> getAllGrouped() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        fp -> fp.getType(),
                        Collectors.mapping(fp ->
                                        new FeaturedProductResponse(
                                                fp.getId(),
                                                fp.getProduct().getProductId(),
                                                fp.getProduct().getName(),
                                                fp.getType(),
                                                fp.getPriority()
                                        ),
                                Collectors.toList()
                        )
                ));
    }
}


