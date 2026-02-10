package com.example.Backend_web.controller;

import com.example.Backend_web.dto.response.InvoiceResponse;
import com.example.Backend_web.entity.Invoice;
import com.example.Backend_web.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    //Enpoint Lấy Hóa đơn theo Mã đơn hàng
    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long orderId){
        return ResponseEntity.ok(invoiceService.getInvoiceByOrderId(orderId));
    }

    //Endpoint Tạo hóa đơn cho đơn hàng của khách hàng
    @PostMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponse> createInvoice(@PathVariable Long orderId){
        return ResponseEntity.ok(invoiceService.createInvoice(orderId));
    }
}
