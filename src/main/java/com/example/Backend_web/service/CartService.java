package com.example.Backend_web.service;

import com.example.Backend_web.entity.*;
import com.example.Backend_web.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductVariantRepository variantRepo;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    //@Autowired
    //private ProductVariantRepository variantRepo;

//    private String getVariantImage(ProductVariant variant) {
//        if (variant == null || variant.getImages() == null || variant.getImages().isEmpty()) {
//            return null;
//        }
//        return variant.getImages().get(0).getImageUrl(); // lấy ảnh đầu tiên
//    }


    // ✅ Thêm sản phẩm vào giỏ
    public Cart addToCart(Long userId, Integer variantId, Integer quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
//        ProductVariant variant = variantRepo.findById(variantId)
//                .orElseThrow(() -> new RuntimeException("Variant not found"));
        ProductVariant variant = variantRepo.findVariantWithImages(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));


        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepo.save(newCart);
        });

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getVariant().getVariantId().equals(variantId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepo.save(existingItem.get());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .variant(variant)
                    .quantity(quantity)
                    .price(variant.getPrice())
                    //.image(getVariantImage(variant)) // ✅ Dùng variant trực tiếp
                    .build();
            cartItemRepo.save(newItem);
        }

        return cartRepo.findById(cart.getId()).get();
    }

    // Hàm lấy ảnh của từng sản phẩm để hiển thị lên giỏ hàng
    private String getVariantImage(ProductVariant variant) {
        if (variant == null) return null;

        // Ưu tiên lấy ảnh riêng của variant (theo màu, dung lượng, ...)
        if (variant.getImages() != null && !variant.getImages().isEmpty()) {
            return variant.getImages().get(0).getImageUrl();
        }

        // Nếu không có ảnh riêng → fallback sang ảnh của sản phẩm gốc
        if (variant.getProduct() != null &&
                variant.getProduct().getVariants() != null &&
                !variant.getProduct().getVariants().isEmpty()) {

            for (ProductVariant pv : variant.getProduct().getVariants()) {
                if (pv.getImages() != null && !pv.getImages().isEmpty()) {
                    return pv.getImages().get(0).getImageUrl();
                }
            }
        }

        return null;
    }



    // ✅ Lấy giỏ hàng của user
    public Cart getCartByUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    // ✅ Thanh toán
    public void checkout(Long userId) {
        Cart cart = getCartByUser(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalPrice(cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        orderRepo.save(order);

        // Tạo chi tiết đơn hàng
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setVariant(item.getVariant());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItemRepo.save(orderItem);
        }

        // Xóa giỏ hàng sau khi thanh toán
        cartItemRepo.deleteAll(cart.getItems());
    }

    // ✅ Xóa sản phẩm khỏi giỏ hàng
    public Cart removeCartItem(Long userId, Integer variantId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Tìm sản phẩm trong giỏ
        Optional<CartItem> itemOpt = cart.getItems().stream()
                .filter(i -> i.getVariant().getVariantId().equals(variantId))
                .findFirst();

        if (itemOpt.isPresent()) {
            // ✅ Xóa khỏi list items
            cart.getItems().remove(itemOpt.get());
            // Save cart, JPA sẽ tự động xóa CartItem khỏi DB nhờ orphanRemoval
            cartRepo.save(cart);
        }

        // Trả về cart mới nhất
        return cartRepo.findById(cart.getId()).orElse(cart);
    }



}
