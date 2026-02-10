package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.CheckoutRequest;
import com.example.Backend_web.dto.request.UpdateOrderStatusRequest;
import com.example.Backend_web.dto.response.OrderResponse;
import com.example.Backend_web.entity.Order;
import com.example.Backend_web.enums.OrderStatus;
import com.example.Backend_web.mapper.OrderMapper;
import com.example.Backend_web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;


    // Checkout toàn bộ giỏ hàng
//    @PostMapping("/checkout/{userId}")
//    public ResponseEntity<OrderResponse> checkout(@PathVariable Long userId) {
//        Order order = orderService.checkout(userId);
//        return ResponseEntity.ok(orderMapper.toResponse(order));
//    }
//
//    // Checkout từng sản phẩm
//    @PostMapping("/checkout/partial")
//    public ResponseEntity<OrderResponse> checkoutPartial(@RequestBody CheckoutRequest request) {
//        Order order = orderService.checkoutPartial(request.getUserId(), request.getVariantIds());
//        return ResponseEntity.ok(orderMapper.toResponse(order));
//    }

    // Checkout (toàn bộ hoặc từng sản phẩm)
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest request) {
        Order order = orderService.checkout(request);
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            orderService.confirmPaymentSuccess(order.getId());
        }

        return ResponseEntity.ok(orderMapper.toResponse(order));
    }



    // ================= ADMIN =================

    // User xem đơn hàng của chính mình
//    @GetMapping("/my")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getMyOrders() {
//        return ResponseEntity.ok(
//                orderService.getMyOrders().stream()
//                        .map(orderMapper::toResponse)
//                        .toList()
//        );
//    }

    // ================= ADMIN =================

    // Admin – xem tất cả đơn hàng
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(
                orderService.getAllOrders(page, size, status, keyword)
                        //.map(orderMapper::toResponse)
        );
    }

    // Admin – xem chi tiết đơn hàng
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(
                orderMapper.toResponse(orderService.getOrderDetail(orderId))
        );
    }

    // Admin – cập nhật trạng thái đơn hàng
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long orderId,
            //@RequestBody UpdateOrderStatusRequest request
            @RequestParam OrderStatus status
            ) {
        //orderService.updateOrderStatus(orderId, request.getStatus());
        orderService.updateStatus(orderId, status);
        return ResponseEntity.ok("Cap nhat trang thai don hang thanh cong");
    }

//    @PostMapping("/complete/{orderId}")
//    public String completeOrder(@PathVariable Long orderId) {
//        orderService.completeOrder(orderId);
//        return "Don hang da duoc thanh toan va email da duoc gui";
//    }
}
