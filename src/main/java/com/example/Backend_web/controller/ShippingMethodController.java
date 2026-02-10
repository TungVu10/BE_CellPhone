package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.ShippingMethodRequest;
import com.example.Backend_web.dto.response.ShippingMethodResponse;
import com.example.Backend_web.service.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    //Endpoint Thêm mới phương thức vận chuyển
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createShippingMethod(@RequestBody ShippingMethodRequest request){
        return ResponseEntity.ok(shippingMethodService.createShippingMethod(request));
    }

    //Endpoint Lấy danh sách tất cả các phương thức
    @GetMapping
    public List<ShippingMethodResponse> getAllShippingMethods(){
        return shippingMethodService.getAllShippingMethods();
    }

    //Endpoint Update phương thức vận chuyển
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShippingMethodResponse> updateShippingMethod(@PathVariable Long id, @RequestBody ShippingMethodRequest request){
        return ResponseEntity.ok(shippingMethodService.updateShippingMethod(id, request));
    }

    //Endpoint Xóa phương thức vận chuyển
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteShippingMethod(@PathVariable Long id){
        shippingMethodService.deleteShippingMethod(id);
        return ResponseEntity.noContent().build();
    }
}
