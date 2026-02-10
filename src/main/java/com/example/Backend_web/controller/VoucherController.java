package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.ApplyVoucherRequest;
import com.example.Backend_web.dto.request.VoucherRequest;
import com.example.Backend_web.dto.response.ApplyVoucherResponse;
//import com.example.Backend_web.dto.response.CreateVoucherResponse;
import com.example.Backend_web.dto.response.VoucherResponse;
//import com.example.Backend_web.service.VoucherService;
import com.example.Backend_web.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    //Endpoint Tạo mới Voucher
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createVoucher(@RequestBody VoucherRequest request){
//        voucherService.createVoucher(request);
//        return ResponseEntity.ok("Tạo Voucher thành công");
        return ResponseEntity.ok(voucherService.createVoucher(request));
    }

    //Endpoint Áp dụng Voucher
    @PostMapping("/apply")
    public ResponseEntity<ApplyVoucherResponse> applyVoucher(@RequestBody ApplyVoucherRequest request){
        return ResponseEntity.ok(voucherService.applyVoucher(request));
    }

    //Endpoint lấy danh sách Voucher
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<VoucherResponse> getAllVouchers(){
        return voucherService.getAllVoucher();
    }

    //Endpoint xóa Voucher
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id){
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    //Endpoint Sửa Voucher
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoucherResponse> updateVoucher(@PathVariable Long id,@RequestBody VoucherRequest request){
        return ResponseEntity.ok(voucherService.updateVoucher(id,request));
    }

}
