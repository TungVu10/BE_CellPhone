package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.request.ShippingMethodRequest;
import com.example.Backend_web.dto.response.ShippingMethodResponse;
import com.example.Backend_web.entity.ShippingMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMethodMapper {
    ShippingMethod toShippingMethod(ShippingMethodRequest request);

    ShippingMethodResponse toShippingMethodResponse(ShippingMethod shippingMethod);
}
