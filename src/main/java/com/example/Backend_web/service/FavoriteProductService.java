package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.FavoriteProduct;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.User;
import com.example.Backend_web.mapper.ProductMapper;
import com.example.Backend_web.repository.FavoriteProductRepository;
import com.example.Backend_web.repository.ProductRepository;
import com.example.Backend_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final ProductMapper productMapper;


    //Thêm Sản phẩm yêu thích
    public void addFavorite(Integer productId, Long userid){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("San pham khong ton tai"));

        User user = userRepository.findById(userid)
                .orElseThrow(()-> new RuntimeException("User khong ton tai"));

        if(favoriteProductRepository.existsByUserAndProduct(user, product)){
            throw new RuntimeException("Da ton tai San pham yeu thich nay");
        }

        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setUser(user);
        favoriteProduct.setProduct(product);
        favoriteProduct.setCreatedAt(LocalDateTime.now());

        favoriteProductRepository.save(favoriteProduct);
    }

    //Lấy danh sách Sản phẩm yêu thích của từng Khách hàng
    public List<ProductResponse> getFavoriteProducts(Long userId){
        List<FavoriteProduct> favoriteProducts = favoriteProductRepository.findByUser_Id(userId);
        return favoriteProducts.stream()
                .map(fp -> productMapper.toProductResponse(fp.getProduct()))
                .toList();

    }

    //Xóa, Bỏ chọn Sản phẩm yêu thích
    public void deleteFavoriteProduct(Integer productId, Long userid){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("San pham khong ton tai"));
        User user = userRepository.findById(userid)
                .orElseThrow(()-> new RuntimeException("User khong ton tai"));

        FavoriteProduct favoriteProduct = favoriteProductRepository
                .findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("San pham chua duoc yeu thich"));
        favoriteProductRepository.delete(favoriteProduct);
    }
}
