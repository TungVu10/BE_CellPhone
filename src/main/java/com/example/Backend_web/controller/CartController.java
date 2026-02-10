package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.CartRequest;
import com.example.Backend_web.dto.request.UpdateCartQuantityRequest;
import com.example.Backend_web.dto.response.CartResponse;
import com.example.Backend_web.entity.Cart;
import com.example.Backend_web.mapper.CartMapper;
import com.example.Backend_web.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    //  Thêm sản phẩm vào giỏ
//    @PostMapping("/add")
//    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
//        Cart cart = cartService.addToCart(
//                request.getUserId(),
//                request.getVariantId(),
//                request.getQuantity()
//        );
//        return ResponseEntity.ok(cartMapper.toResponse(cart));
//    }

    //Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        Cart cart = cartService.addToCart(request.getUserId(), request.getVariantId(), request.getQuantity());
        // map cart entity sang DTO (CartResponse) để bao gồm items đầy đủ
        return ResponseEntity.ok(cartMapper.toResponse(
                cartService.getCartByUser(cart.getUser().getId()) // fetch lại cart mới nhất từ DB
        ));
    }

    //  Lấy giỏ hàng của user
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    //  Thanh toán
//    @PostMapping("/checkout/{userId}")
//    public ResponseEntity<String> checkout(@PathVariable Long userId) {
//        cartService.checkout(userId);
//        return ResponseEntity.ok("Checkout successfully!");
//    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{userId}/remove/{variantId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Integer variantId) {

        Cart cart = cartService.removeCartItem(userId, variantId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }
    // Tăng giảm số lượng đơn hàng
    //@PostMapping("/update")
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(@RequestBody UpdateCartQuantityRequest request) {
        Cart cart = cartService.updateQuantity(request.getUserId(), request.getVariantId(), request.getQuantity());
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

}
