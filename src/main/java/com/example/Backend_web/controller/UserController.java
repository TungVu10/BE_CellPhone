package com.example.Backend_web.controller;

import com.example.Backend_web.configuration.SecurityUtil;
import com.example.Backend_web.dto.request.ApiResponse;
import com.example.Backend_web.dto.request.UserCreationRequest;
import com.example.Backend_web.dto.request.UserUpdateRequest;
import com.example.Backend_web.dto.response.OrderResponse;
import com.example.Backend_web.dto.response.UserResponse;
import com.example.Backend_web.entity.Order;
import com.example.Backend_web.service.OrderService;
import com.example.Backend_web.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    final UserService userService;
    private final OrderService orderService;

    //Thêm thông tin người dùng
//    @PostMapping
//    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.createUser(request))
//                .build();
//    }

    // API: Thêm người dùng mới (đăng ký tài khoản)
    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    //Lấy tất cả thông tin khách hàng
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
//    ApiResponse<List<UserResponse>> getUsers() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        log.info("Username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()) );
//
//        return ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build();
//    }
    public List<UserResponse> getAllUsers() {
        return userService.getUsers();
    }

    //Lấy thông tin khách hàng theo id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Endpoint Khách hàng xem thông tin tài khoản của mình
    @GetMapping("/me")
    public UserResponse getMyProfile(){
        return userService.getMyProfile();

    }

    //Endpoint Khách hàng xem danh sách đơn hàng của mình
    @GetMapping("/me/orders")
    public List<OrderResponse> getMyOrders(){
        return userService.getMyOrders();
    }

    //EndPoint Khách hàng lấy chi tiết đơn hàng
    @GetMapping("/my-order/{orderId}")
    public OrderResponse getMyOrderById(@PathVariable Long orderId){
        return userService.getMyOrderDetail(orderId);
    }

    //Endpoint Khách hàng hủy đơn hàng
    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id){
        userService.cancelOrder(id);
        return ResponseEntity.ok("Huy don hang thanh cong");
    }

    //Endpoint khách hàng cập nhật thông tin của mình
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        UserResponse update = userService.updateUser(id, request);
        return ResponseEntity.ok(update);
    }


}
