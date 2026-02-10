package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.response.*;
import com.example.Backend_web.entity.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

//    @Mapping(target = "customerName", source = "user.fullname")
//    @Mapping(target = "customerPhone", source = "user.phone")
//    @Mapping(target = "customerEmail", source = "user.email")
//    @Mapping(target = "customerAddress", source = "user.address")


//    @Mapping(target = "customerName", source = "receiverName")
//    @Mapping(target = "customerPhone", source = "receiverPhone")
//    @Mapping(target = "customerEmail", source = "receiverEmail")
//    @Mapping(target = "customerAddress", source = "receiverAddress")
//    @Mapping(target = "customerPaymentMethod", source = "paymentMethod")
//
//    @Mapping(target = "items", source = "items")
//    OrderResponse toResponse(Order order);

    @Mapping(target = "customerName", source = "receiverName")
    @Mapping(target = "customerPhone", source = "receiverPhone")
    //@Mapping(target = "customerEmail", source = "user.email")
    @Mapping(target = "customerEmail", source = "receiverEmail")
    @Mapping(target = "customerAddress", source = "receiverAddress")
    @Mapping(target = "customerPaymentMethod", source = "paymentMethod")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "hasInvoice", ignore = true) // set ở service
    OrderResponse toResponse(Order order);



    @Mapping(target = "variantId", expression = "java(item.getVariant().getVariantId())")
    @Mapping(target = "variantName", source = "variant.name")
    //@Mapping(target = "variantName", source = "variant.variantName")

    @Mapping(target = "image", expression = "java(getVariantImage(item.getVariant()))")
    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponseList(List<OrderItem> items);

    default String getVariantImage(ProductVariant variant) {
        if (variant.getImages() != null && !variant.getImages().isEmpty()) {
            return variant.getImages().get(0).getImageUrl();
        }
        if (variant.getProduct() != null && variant.getProduct().getVariants() != null) {
            for (ProductVariant pv : variant.getProduct().getVariants()) {
                if (pv.getImages() != null && !pv.getImages().isEmpty()) {
                    return pv.getImages().get(0).getImageUrl();
                }
            }
        }
        return null;
    }

    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "orderDate", source = "orderDate")
    OrderResponse toOrderResponse(Order order);
}
