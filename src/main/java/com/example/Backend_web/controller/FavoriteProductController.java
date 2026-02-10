package com.example.Backend_web.controller;

import com.example.Backend_web.dto.response.ProductResponse;
import com.example.Backend_web.entity.FavoriteProduct;
import com.example.Backend_web.entity.User;
import com.example.Backend_web.repository.FavoriteProductRepository;
import com.example.Backend_web.repository.UserRepository;
import com.example.Backend_web.service.AuthenticationService;
import com.example.Backend_web.service.FavoriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteProductController {
    private final FavoriteProductRepository favoriteProductRepository;
    private final FavoriteProductService favoriteProductService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    //EndPoint Thêm Sản phẩm yêu thích
    @PostMapping("/{productId}")
//    public ResponseEntity<?> addFavoriteProduct(@PathVariable Integer productId, Long userId) {
//        favoriteProductService.addFavorite(productId, userId);
//        return ResponseEntity.ok("Them San pham yeu thich thanh cong");
//    }
    public ResponseEntity<?> addFavorite(@PathVariable Integer productId) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Unauthenticated");
        }

        String username = auth.getName(); // email hoặc username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User khong ton tai"));

        favoriteProductService.addFavorite(productId, user.getId());
        return ResponseEntity.ok("Them san pham yeu thich thanh cong");
    }


    //EndPoint Lấy danh sách Sản Phẩm Khách hàng yêu thích
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ProductResponse>> getFavoriteProducts(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteProductService.getFavoriteProducts(userId));
    }

    //EndPoint Xóa Sản phẩm yêu thích
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Integer productId) {
       //Lấy User từ JWT
        User user = authenticationService.getCurrentUser();
        favoriteProductService.deleteFavoriteProduct(productId, user.getId());
        return ResponseEntity.ok("Đã Xóa khỏi danh sách Sản phẩm yêu thích");
    }
}
