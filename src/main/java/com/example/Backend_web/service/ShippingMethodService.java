package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.ShippingMethodRequest;
//import com.example.Backend_web.dto.response.CreateVoucherResponse;
import com.example.Backend_web.dto.response.ShippingMethodResponse;
import com.example.Backend_web.entity.Product;
import com.example.Backend_web.entity.ShippingMethod;
import com.example.Backend_web.entity.Voucher;
import com.example.Backend_web.mapper.ShippingMethodMapper;
import com.example.Backend_web.repository.ShippingMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;
    private final ShippingMethodMapper shippingMethodMapper;

    //Tạo mới phương thức vận chuyển
    public ShippingMethodResponse createShippingMethod(ShippingMethodRequest request){
//        List<ShippingMethod> shippingMethods = shippingMethodRepository.findAllById(request.get);
//        if (products.isEmpty()) {
//            throw new RuntimeException("Danh sach san pham khong hop le");
//        }

        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setName(request.getName());
        shippingMethod.setDescription(request.getDescription());
        shippingMethod.setEstimateDays(request.getEstimateDays());
        shippingMethod.setStatus(true);

        //  GÁN PRODUCTS
        //voucher.setProducts(products);

        //  SAVE & LẤY ID
        ShippingMethod savedShippingMethod = shippingMethodRepository.save(shippingMethod);

//        return new CreateVoucherResponse(
//                savedShippingMethod.getId(),   //  KHÔNG NULL
//                savedShippingMethod.getDescription(),
//                savedShippingMethod.getEstimateDays()
//        );
        return new ShippingMethodResponse(
                savedShippingMethod.getName(),
                savedShippingMethod.getDescription(),
                savedShippingMethod.getEstimateDays()
        );
    }

    //Lấy danh sách tất cả các phương thức vận chuyển
    public List<ShippingMethodResponse> getAllShippingMethods(){
        return shippingMethodRepository.findAll()
                .stream().map(shippingMethodMapper::toShippingMethodResponse).toList();
    }



    //Sửa phương thức vận chuyển
    public ShippingMethodResponse updateShippingMethod(Long id, ShippingMethodRequest request){
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong ton tai phuong thuc van chuyen nay"));

        shippingMethod.setName(request.getName());
        shippingMethod.setDescription(request.getDescription());
        shippingMethod.setEstimateDays(request.getEstimateDays());

        ShippingMethod update = shippingMethodRepository.save(shippingMethod);
        return shippingMethodMapper.toShippingMethodResponse(update);
    }

    //Xóa phương thức vận chuyển
    public void deleteShippingMethod(Long id){
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong ton tai phuong thuc van chuyen nay"));
        shippingMethodRepository.delete(shippingMethod);
    }
}
