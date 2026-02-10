package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.HotProduct;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.mapper.ProductMapper;
import com.example.Backend_web.repository.HotProductRepository;
import com.example.Backend_web.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotProductService {
    private final HotProductRepository hotProductRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final FeaturedProductService featuredProductService;


    //Lấy danh sách Sản phẩm HOT
//    public List<ProductResponse> getHotProducts() {
//        return hotProductRepository.findHotProducts()
//                .stream()
//                .map(productMapper::toProductResponse)
//                .toList();
//    }
    public List<ProductResponse> getHotProducts() {
        return hotProductRepository.findHotProducts()
                .stream()
                .map(product -> {
                    ProductResponse res = productMapper.toProductResponse(product);
                    res.setIsHot(true); //  chắc chắn là HOT
                    return res;
                })
                .toList();
    }


    //Set Sản phẩm HOT (Thêm Sản phẩm HOT)
    public void setHotProduct(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("San Pham khong ton tai"));
        if (hotProductRepository.existsByProduct(product)){
            throw new RuntimeException("San Pham da la San pham HOT");
        }
        HotProduct hotProduct = new HotProduct();
        hotProduct.setProduct(product);
        hotProduct.setCreatedAt(LocalDateTime.now());
        hotProductRepository.save(hotProduct);

    }

    //Xóa Sản phẩm khỏi danh sách Sản phẩm HOT
    public void deleteHotProduct(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("San Pham khong ton tai"));
//        if (hotProductRepository.existsByProduct(product)){
//            throw new RuntimeException("San Pham da la San pham HOT");
//        }
        HotProduct hotProduct = hotProductRepository
                .findByProduct(product)
                        .orElseThrow(() -> new RuntimeException("San Pham khong phai HOT"));

        //productService.deleteProduct(productId);
        hotProductRepository.delete(hotProduct);
    }
}
