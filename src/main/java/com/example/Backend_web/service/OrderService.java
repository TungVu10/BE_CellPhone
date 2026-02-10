package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.CheckoutItemRequest;
import com.example.Backend_web.dto.request.CheckoutRequest;
import com.example.Backend_web.dto.response.OrderItemResponse;
import com.example.Backend_web.dto.response.OrderResponse;
import com.example.Backend_web.entity.*;
import com.example.Backend_web.enums.DiscountType;
import com.example.Backend_web.enums.OrderStatus;
import com.example.Backend_web.mapper.OrderMapper;
import com.example.Backend_web.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final OrderRepository orderRepository;
    //private final EmailService emailService;
    private final GmailApiService gmailApiService;
    private final InvoiceRepository invoiceRepository;
    private final OrderMapper orderMapper;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;

    //Thanh toán đơn hàng
    @Transactional
    public Order checkout(CheckoutRequest request) {

        // Thanh toán ngay
        if (Boolean.TRUE.equals(request.getBuyNow())) {

            if (request.getItems() == null || request.getItems().isEmpty()) {
                throw new RuntimeException("Buy now items is required");
            }

            List<CartItem> fakeItems = new ArrayList<>();

            for (CheckoutItemRequest i : request.getItems()) {

                ProductVariant variant = productVariantRepository
                        .findById(i.getVariantId())
                        .orElseThrow(() -> new RuntimeException("Variant not found"));

                CartItem item = CartItem.builder()
                        .variant(variant)
                        .quantity(i.getQuantity())   //  ĐÂY MỚI LÀ QUANTITY TỪ FE
                        .price(variant.getPrice())
                        .build();

                fakeItems.add(item);
            }

            return createOrder(null, fakeItems, request);
        }


        // Thanh toán qua Giỏ hàng
        Cart cart = cartRepo.findByUser(User.builder().id(request.getUserId()).build())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> itemsToCheckout;
        if (request.getVariantIds() == null || request.getVariantIds().isEmpty()) {
            itemsToCheckout = cart.getItems(); // checkout toàn bộ
        } else {
            itemsToCheckout = cart.getItems().stream()
                    .filter(item -> request.getVariantIds().contains(item.getVariant().getVariantId()))
                    .toList();
        }

        if (itemsToCheckout.isEmpty())
            throw new RuntimeException("No items selected for checkout");

        return createOrder(cart, itemsToCheckout, request);
    }

    // Tạo order và gán thông tin nhận hàng + phương thức thanh toán
//    private Order createOrder(Cart cart, List<CartItem> itemsToCheckout, CheckoutRequest request) {
//        Order order = Order.builder()
//                .user(cart.getUser())
//                .orderDate(LocalDateTime.now())
//                .status(OrderStatus.valueOf("PENDING"))
//                .totalPrice(BigDecimal.ZERO)
//                .build();
//
//        // Gán thông tin nhận hàng + phương thức thanh toán
//        order.setReceiverName(request.getFullName());
//        order.setReceiverPhone(request.getPhone());
//        order.setReceiverAddress(request.getAddress());
//        order.setReceiverEmail(request.getEmail());
//        order.setPaymentMethod(request.getPaymentMethod());
//
//        order = orderRepo.save(order);
//
//        BigDecimal total = BigDecimal.ZERO;
//        for (CartItem item : itemsToCheckout) {
//            ProductVariant variant = item.getVariant();
//            int buyQty = item.getQuantity();
//            if(variant.getQuantity() < buyQty){
//                throw new RuntimeException("San pham " + variant.getName()+" khong du hang ton kho");
//            }
//            //Trừ hàng tồn kho
//            variant.setQuantity(variant.getQuantity() - buyQty);
//            productVariantRepository.save(variant);
//            OrderItem orderItem = OrderItem.builder()
//                    .order(order)
//                    .variant(item.getVariant())
//                    .quantity(item.getQuantity())
//                    .price(item.getPrice())
//                    .build();
//            orderItemRepo.save(orderItem);
//            //total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
//            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(buyQty)));
//        }
//
//        order.setTotalPrice(total);
//        order.setItems(orderItemRepo.findByOrder(order));
//
//
//
//        //Lấy thông tin khách hàng khi thanh toán
//
//        //order = orderRepo.save(order);
//        // Xóa các item đã thanh toán khỏi cart
//        cart.getItems().removeAll(itemsToCheckout);
//        cartRepo.save(cart);
//
//        return order;
//    }


    private Order createOrder(Cart cart, List<CartItem> itemsToCheckout, CheckoutRequest request) {

        //  LẤY USER TỪ REQUEST – không phụ thuộc cart
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalPrice(BigDecimal.ZERO)
                .build();

        // Gán thông tin nhận hàng
        order.setReceiverName(request.getFullName());
        order.setReceiverPhone(request.getPhone());
        order.setReceiverAddress(request.getAddress());
        order.setReceiverEmail(request.getEmail());
        order.setPaymentMethod(request.getPaymentMethod());

        order = orderRepo.save(order);

        // ===== XỬ LÝ ITEM =====
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : itemsToCheckout) {

            ProductVariant variant = item.getVariant();
            int buyQty = item.getQuantity();

            // 1. Check tồn kho
            if (variant.getQuantity() < buyQty) {
                throw new RuntimeException(
                        "San pham " + variant.getName() + " khong du hang ton kho"
                );
            }

            // 2. Trừ tồn kho
            variant.setQuantity(variant.getQuantity() - buyQty);
            productVariantRepository.save(variant);

            // 3. Tạo OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variant(variant)
                    .quantity(buyQty)
                    .price(item.getPrice())
                    .build();

            orderItemRepo.save(orderItem);

            // 4. Tính total
            total = total.add(
                    item.getPrice().multiply(BigDecimal.valueOf(buyQty))
            );
        }

        // Xử lý Voucher
        if (request.getVoucherCode() != null) {

            Voucher voucher = voucherRepository
                    .findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new RuntimeException("Voucher khong ton tai"));

            LocalDateTime now = LocalDateTime.now();

            // 7. Check hạn
//            if (voucher.getStartDate().isAfter(now) ||
//                    voucher.getEndDate().isBefore(now)) {
//                throw new RuntimeException("Voucher het han");
//            }

            if(voucher.getStartDate().isAfter(now)){
                throw new RuntimeException("Voucher chưa đến thời gian được sử dụng");
            }
            if(voucher.getEndDate().isBefore(now)){
                throw new RuntimeException("Voucher hết hạn");
            }

            // 8. Check tổng lượt hệ thống (quantity)
            if (voucher.getUsedCount() >= voucher.getQuantity()) {
                throw new RuntimeException("Voucher da het luot toan he thong");
            }

            // 9. Check mỗi user được dùng mấy lần (usageLimit)
            long usedByUser = orderRepo.countVoucherUsedByUser(
                    voucher.getVoucherId(),
                    user.getId()
            );

            if (usedByUser >= voucher.getUsageLimit()) {
                throw new RuntimeException(
                        "Ban da su dung voucher nay toi da "
                                + voucher.getUsageLimit() + " lan"
                );
            }

            // 10. Check minOrder
            if (total.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new RuntimeException(
                        "Don hang chua dat gia tri toi thieu de dung voucher"
                );
            }

            // 11. Tính giảm giá
            BigDecimal discount = BigDecimal.ZERO;

            if (voucher.getDiscountType() == DiscountType.PERCENT) {

                discount = total.multiply(
                        voucher.getDiscountValue()
                                .divide(BigDecimal.valueOf(100))
                );

            } else {
                discount = voucher.getDiscountValue();
            }

            total = total.subtract(discount);

            // 12. Gán voucher vào order
            order.setVoucher(voucher);

            // 13. Tăng usedCount
            voucher.setUsedCount(voucher.getUsedCount() + 1);
            voucherRepository.save(voucher);
        }

        order.setTotalPrice(total);
        order.setItems(orderItemRepo.findByOrder(order));

        // ===== CHỈ xóa cart nếu checkout từ giỏ =====
//        if (cart != null) {
//            cart.getItems().removeAll(itemsToCheckout);
//            cartRepo.save(cart);
//        }
        // ===== CHỈ xóa cart nếu checkout từ giỏ =====
        if (cart != null) {

            List<Long> ids = itemsToCheckout.stream()
                    .map(CartItem::getId)
                    .toList();

            cart.getItems().removeIf(item -> ids.contains(item.getId()));

            cartRepo.saveAndFlush(cart);
        }


        return order;
    }




    // Xác nhận thanh toán + Gửi Email
//    @Transactional
//    public void confirmPaymentSuccess(Long orderId) {
//
//        System.out.println(">>> confirmPaymentSuccess CALLED, orderId = " + orderId);
//
//        Order order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        order.setStatus(OrderStatus.PAID);
//        orderRepo.save(order);
//
//        try {
//            System.out.println(">>> BAT DAU GUI MAIL...");
//            gmailApiService.sendOrderSuccessEmail(
//                    order.getUser().getEmail(),
//                    order.getReceiverName(),
//                    order.getId(),
//                    order.getTotalPrice().toString()
//            );
//            System.out.println(">>> GUI MAIL THANH CONG");
//        } catch (Exception e) {
//            System.err.println(">>> LOI GUI MAIL: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @Transactional
    public void confirmPaymentSuccess(Long orderId) {
        System.out.println(">>> VAO confirmPaymentSuccess, orderId = " + orderId);
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ❗ CHỈ gửi mail nếu CHƯA gửi
        if (order.getStatus() != OrderStatus.PAID) {

            order.setStatus(OrderStatus.PAID);
            orderRepo.save(order);
            String displayName = order.getReceiverName();

            if (displayName == null || displayName.isBlank()) {
                displayName = order.getUser().getUsername();
            }


            try {
                gmailApiService.sendOrderSuccessEmail(
                        order.getUser().getEmail(),   // EMAIL PHẢI KHÔNG NULL
                        //order.getReceiverName(),
                        displayName,
                        order.getId(),
                        order.getTotalPrice().toString()
                );
                System.out.println(">>> DA GUI MAIL XAC NHAN DON");
            } catch (Exception e) {
                System.err.println(">>> LOI GUI MAIL: " + e.getMessage());
            }
        }
    }





    // =============== USER GET OWN ORDERS ========================

    public List<Order> getMyOrders(Long userId) {
        return orderRepo.findByUserIdOrderByOrderDateDesc(userId);
    }



    // Admin lấy danh sách tất cả các đơn hàng
//    public Page<Order> getAllOrders(int page, int size, String status, String keyword) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
//
//        boolean noKeyword = (keyword == null || keyword.trim().isEmpty());
//        boolean noStatus = (status == null || status.trim().isEmpty());
//
//        if (noKeyword && noStatus) {
//            return orderRepo.findAll(pageable);
//        }
//
//        return orderRepo.searchOrders(status, keyword, pageable);
//    }


    public Page<OrderResponse> getAllOrders(
            int page,
            int size,
            String status,
            String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orders;

        boolean noKeyword = (keyword == null || keyword.trim().isEmpty());
        boolean noStatus = (status == null || status.trim().isEmpty());

        if (noKeyword && noStatus) {
            orders = orderRepo.findAll(pageable);
        } else {
            orders = orderRepo.searchOrders(status, keyword, pageable);
        }

        return orders.map(order -> {
            OrderResponse res = orderMapper.toResponse(order);
            res.setHasInvoice(
                    invoiceRepository.existsByOrderId(order.getId())
            );
            return res;
        });
    }



    // Admin lấy thông tin chi tiết đơn hàng
    public Order getOrderDetail(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    // ================= ADMIN — UPDATE ORDER STATUS =================
//    @Transactional
//    public void updateOrderStatus(Long orderId, String newStatus) {
//
//        Order order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//
//        order.setStatus(newStatus);
//
//        orderRepo.save(order);
//    }

    // Update Trạng thái đơn hàng
    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay don hang"));

        // Không cho quay lại trạng thái cũ (optional)
        order.setStatus(status);

        orderRepository.save(order);
    }

    //Gửi về email Khách hàng khi đơn hàng được thanh toán (COMPLETED)
//    public void completeOrder(Long orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Don hang khong ton tai"));
//
//        order.setStatus(OrderStatus.valueOf("COMPLETED"));
//        orderRepository.save(order);
//
//        // Gửi email thông báo
//        String subject = "Xac nhan don hang #" + order.getId();
//        String content = "<h1>Cam on ban da thanh toan!</h1>" +
//                "<p>Don hang cua ban da duoc xac nhan.</p>" +
//                "<p>Tong tien: " + order.getTotalPrice() + " VND</p>";
//
//        emailService.sendEmail(order.getReceiverEmail(), subject, content);
//    }
}
