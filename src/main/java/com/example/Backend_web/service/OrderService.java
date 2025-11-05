package com.example.Backend_web.service;

import com.example.Backend_web.entity.*;
import com.example.Backend_web.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public Order checkout(Long userId) {
        Cart cart = cartRepo.findByUser(
                User.builder().id(userId).build()
        ).orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalPrice(BigDecimal.ZERO)
                .build();
        order = orderRepo.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variant(item.getVariant())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
            orderItemRepo.save(orderItem);
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setTotalPrice(total);
        orderRepo.save(order);

        // Xóa giỏ hàng sau thanh toán
        cartRepo.delete(cart);

        return order;
    }
}

