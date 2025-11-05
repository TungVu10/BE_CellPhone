package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.response.*;
import com.example.Backend_web.entity.*;
import com.example.Backend_web.enums.Role;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "items", source = "items")
    CartResponse toResponse(Cart cart);

    // Ánh xạ User -> UserResponse
    @Mapping(target = "id", expression = "java(String.valueOf(user.getId()))")
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponse toUserResponse(User user);

    // Ánh xạ từng item trong giỏ
    @Mapping(target = "variantId", expression = "java(item.getVariant().getVariantId())") // ✅ Thêm variantId
    @Mapping(target = "variantName", source = "variant.name")
    @Mapping(target = "image", expression = "java(getImageUrl(item.getVariant()))")
    CartItemResponse toCartItemResponse(CartItem item);

    // Hàm hỗ trợ convert roles
    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    // ✅ Hàm lấy ảnh đúng chuẩn từ variant hoặc product cha
    default String getImageUrl(ProductVariant variant) {
        if (variant == null) return null;

        // Nếu variant có ảnh riêng
        if (variant.getImages() != null && !variant.getImages().isEmpty()) {
            return variant.getImages().get(0).getImageUrl();
        }

        // Nếu variant không có ảnh → tìm ảnh đầu tiên của các variant trong product cha
        if (variant.getProduct() != null && variant.getProduct().getVariants() != null) {
            for (ProductVariant pv : variant.getProduct().getVariants()) {
                if (pv.getImages() != null && !pv.getImages().isEmpty()) {
                    return pv.getImages().get(0).getImageUrl();
                }
            }
        }

        // Không có ảnh nào
        return null;
    }
}
