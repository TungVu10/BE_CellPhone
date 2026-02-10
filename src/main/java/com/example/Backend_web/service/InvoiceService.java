package com.example.Backend_web.service;

import com.example.Backend_web.dto.response.InvoiceItemResponse;
import com.example.Backend_web.dto.response.InvoiceResponse;
import com.example.Backend_web.entity.Invoice;
import com.example.Backend_web.entity.InvoiceItem;
import com.example.Backend_web.entity.Order;
import com.example.Backend_web.enums.OrderStatus;
import com.example.Backend_web.repository.InvoiceRepository;
import com.example.Backend_web.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    //Tạo hóa đơn từ đơn hàng của khách hàng
    @Transactional
    public InvoiceResponse createInvoice(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Don hang khong ton tai"));
        //  Check trạng thái
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new RuntimeException("Don hang chua hoan thanh");
        }

        //  Check đã có hóa đơn chưa
        if (invoiceRepository.existsByOrderId(orderId)) {
            throw new RuntimeException("Don hang da co hoa don");
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceCode("HD" + System.currentTimeMillis());
        invoice.setCustomerName(order.getReceiverName());
        invoice.setCustomerPhone(order.getReceiverPhone());
        invoice.setCustomerEmail(order.getReceiverEmail());
        invoice.setCustomerAddress(order.getReceiverAddress());
        invoice.setCustomerPaymentMethod(order.getPaymentMethod());
        invoice.setTotalAmount(order.getTotalPrice());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setOrder(order);

        List<InvoiceItem> items = order.getItems().stream()
                .map(oi -> {
                    InvoiceItem item = new InvoiceItem();
                    item.setProductName(oi.getVariant().getProduct().getName());
                    item.setPrice(oi.getPrice());
                    item.setQuantity(oi.getQuantity());
                    item.setSubtotal(oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())));
                    item.setInvoice(invoice);
                    return item;
                }).toList();
        invoice.setInvoiceItems(items);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // ✅ Map sang DTO
        return InvoiceResponse.builder()
                .id(savedInvoice.getId())
                .invoiceCode(savedInvoice.getInvoiceCode())
                .customerName(savedInvoice.getCustomerName())
                .customerPhone(savedInvoice.getCustomerPhone())
                .customerEmail(savedInvoice.getCustomerEmail())
                .customerAddress(savedInvoice.getCustomerAddress())
                .customerPaymentMethod(savedInvoice.getCustomerPaymentMethod())
                .totalAmount(savedInvoice.getTotalAmount())
                .createdAt(savedInvoice.getCreatedAt())
                .build();
    }

    //Lấy hóa đơn theo Mã đơn hàng
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {

        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));

        List<InvoiceItemResponse> items = invoice.getInvoiceItems()
                .stream()
                .map(item -> new InvoiceItemResponse(
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceCode(),
                invoice.getCustomerName(),
                invoice.getCustomerEmail(),
                invoice.getCustomerPhone(),
                invoice.getCustomerAddress(),
                invoice.getCustomerPaymentMethod(),
                invoice.getTotalAmount(),
                invoice.getCreatedAt(),
                items
        );
    }

    private String generateInvoiceHTML(Invoice invoice) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Hoa Don Dat Hang</h1>");
        sb.append("<p>Ma hoa don: ").append(invoice.getInvoiceCode()).append("</p>");
        sb.append("<p>Khach hang: ").append(invoice.getCustomerName()).append("</p>");
        sb.append("<p>Email: ").append(invoice.getCustomerEmail()).append("</p>");
        sb.append("<p>So dien thoai: ").append(invoice.getCustomerPhone()).append("</p>");
        sb.append("<p>Dia chi: ").append(invoice.getCustomerAddress()).append("</p>");
        sb.append("<p>Tong tien: ").append(invoice.getTotalAmount()).append(" VND</p>");

        sb.append("<h3>Cac mat hang:</h3>");
        sb.append("<ul>");
        invoice.getOrder().getItems().forEach(item -> {
            sb.append("<li>")
                    .append(item.getVariant().getName())
                    .append(" - SL: ").append(item.getQuantity())
                    .append(" - Gia: ").append(item.getPrice())
                    .append("</li>");
        });
        sb.append("</ul>");

        return sb.toString();
    }
}
