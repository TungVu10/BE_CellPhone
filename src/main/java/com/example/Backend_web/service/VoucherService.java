package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.ApplyVoucherRequest;
import com.example.Backend_web.dto.request.VoucherRequest;
import com.example.Backend_web.dto.response.ApplyVoucherResponse;
//import com.example.Backend_web.dto.response.CreateVoucherResponse;
import com.example.Backend_web.dto.response.VoucherResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.ProductVariant;
import com.example.Backend_web.entity.Voucher;
import com.example.Backend_web.enums.DiscountType;
import com.example.Backend_web.enums.VoucherStatus;
import com.example.Backend_web.mapper.VoucherMapper;
import com.example.Backend_web.repository.ProductRepository;
import com.example.Backend_web.repository.ProductVariantRepository;
import com.example.Backend_web.repository.VoucherRepository;
//import com.example.Backend_web.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final VoucherMapper voucherMapper;

    //Kiểm tra và áp dụng Voucher
    //@Override
    public ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request) {

        Voucher voucher = voucherRepository
                .findByCodeAndStatusTrue(request.getCode())
                .orElseThrow(() -> new RuntimeException("Voucher khong ton tai"));

        LocalDateTime now = LocalDateTime.now();

        // 1. Check thoi gian
        if (now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
            throw new RuntimeException("Voucher da het han hoac chua hieu luc");
        }

        // 2. Check so luong
        if (voucher.getUsedCount() >= voucher.getQuantity()) {
            throw new RuntimeException("Voucher da het luot su dung");
        }

        // 3. Check min order THEO TONG DON
        BigDecimal orderTotal = request.getOrderTotal();

        if (orderTotal.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new RuntimeException("Don hang chua dat gia tri toi thieu");
        }

        // 4. Neu co check theo variant/product
        if (request.getVariantId() != null) {

            ProductVariant variant = productVariantRepository
                    .findById(request.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay variant"));

            Product product = variant.getProduct();
//            System.out.println("===== DEBUG VOUCHER =====");
//            System.out.println("VariantId request: " + request.getVariantId());
//            System.out.println("Product from variant: " + product.getProductId());
//
//            System.out.println("Voucher ap dung cho cac product:");
//            voucher.getProducts().forEach(p ->
//                    System.out.println("- " + p.getProductId())
//            );

            if (!voucher.getProducts().contains(product)) {
                throw new RuntimeException("Voucher khong ap dung cho san pham nay");
            }

//            boolean isValid = voucher.getProducts()
//                    .stream()
//                    .anyMatch(p -> p.getProductId().equals(product.getProductId()));

//            if (!isValid) {
//                throw new RuntimeException("Voucher khong ap dung cho san pham nay");
//            }

        }

        // 5. Tinh tien giam THEO TONG DON
        BigDecimal discountAmount;

        if (voucher.getDiscountType() == DiscountType.PERCENT) {

            discountAmount = orderTotal
                    .multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));

        } else {
            discountAmount = voucher.getDiscountValue();
        }

        // Khong cho giam qua tong tien
        if (discountAmount.compareTo(orderTotal) > 0) {
            discountAmount = orderTotal;
        }

        BigDecimal finalPrice = orderTotal.subtract(discountAmount);

        //  KHONG TANG usedCount O DAY

        return new ApplyVoucherResponse(
                voucher.getCode(),
                orderTotal,
                discountAmount,
                finalPrice,
                "Ap dung voucher thanh cong"
        );
    }





    //Tạo mới Voucher
    //@Override
//    public void createVoucher(VoucherRequest request){
//        if(voucherRepository.existsByCode(request.getCode())){
//            throw new RuntimeException("Voucher đã tồn tại");
//        }
//        List<Product> products = productRepository.findAllById(request.getProductIds());
//
//        if (products.isEmpty()) {
//            throw new RuntimeException("Danh sach san pham khong hop le");
//        }
//        Voucher voucher = new Voucher();
//        voucher.setCode(request.getCode());
//        voucher.setDescription(request.getDescription());
//        voucher.setDiscountType(request.getDiscountType());
//        voucher.setDiscountValue(request.getDiscountValue());
//        voucher.setStartDate(request.getStartDate());
//        voucher.setEndDate(request.getEndDate());
//        voucher.setStatus(true);
//        voucherRepository.save(voucher);
//    }

    //Tạo mới Voucher
    public VoucherResponse createVoucher(VoucherRequest request){

        if (voucherRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Voucher da ton tai");
        }

        List<Product> products = productRepository.findAllById(request.getProductIds());
        if (products.isEmpty()) {
            throw new RuntimeException("Danh sach san pham khong hop le");
        }

        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());

        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setQuantity(request.getQuantity());
        voucher.setUsedCount(0);
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        //voucher.setStatus(true);
        voucher.setStatus(VoucherStatus.ACTIVE);

        //  GÁN PRODUCTS
        voucher.setProducts(products);

        //  SAVE & LẤY ID
        Voucher savedVoucher = voucherRepository.save(voucher);

        return new VoucherResponse(
                savedVoucher.getVoucherId(),   //  KHÔNG NULL
                savedVoucher.getCode(),
                savedVoucher.getDescription(),
                savedVoucher.getDiscountType(),
                savedVoucher.getDiscountValue(),
                savedVoucher.getMinOrderValue(),
                savedVoucher.getQuantity(),
                savedVoucher.getUsedCount(),
                savedVoucher.getUsageLimit(),
                savedVoucher.getStartDate(),
                savedVoucher.getEndDate(),
                savedVoucher.getStatus()
                //"Tao voucher thanh cong"
        );
    }

    //Lấy danh sách tất cả các Voucher
    public List<VoucherResponse> getAllVoucher(){
        return voucherRepository.findAll()
                .stream().map(voucherMapper::toVoucherResponse).toList();
    }

    //Update Voucher
    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong ton tai Voucher")) ;

        voucher.setCode(request.getCode());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());

        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setQuantity(request.getQuantity());
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        //voucher.setStatus(true);
        voucher.setStatus(VoucherStatus.ACTIVE);

        Voucher update = voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(update);
    }

//    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
//        Voucher voucher = voucherRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Khong ton tai Voucher")) ;
//
//        voucher.setUsageLimit(request.getUsageLimit());
//
//        Voucher update = voucherRepository.save(voucher);
//
//        // TRẢ THỦ CÔNG – KHÔNG QUA MAPPER
//        return new VoucherResponse(
//                update.getVoucherId(),
//                update.getCode(),
//                update.getDescription(),
//                update.getDiscountType(),
//                update.getDiscountValue(),
//                update.getMinOrderValue(),
//                update.getQuantity(),
//                update.getUsedCount(),
//                update.getUsageLimit(),      // 👈 CHỖ QUAN TRỌNG
//                update.getStartDate(),
//                update.getEndDate(),
//                update.getStatus()
//        );
//    }


    //Xóa Voucher

    public void deleteVoucher(Long voucherId){
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Voucher khong ton tai"));
        voucherRepository.delete(voucher);
    }
}
