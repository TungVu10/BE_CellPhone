package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.request.VoucherRequest;
//import com.example.Backend_web.dto.response.CreateVoucherResponse;
import com.example.Backend_web.dto.response.VoucherResponse;
import com.example.Backend_web.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest request);

//    VoucherResponse toVoucherResponse(Voucher voucher);
    @Mapping(source = "voucherId", target = "id")
    VoucherResponse toVoucherResponse(Voucher voucher);

    //CreateVoucherResponse toVoucherResponse(Voucher voucher);
}
