package com.example.Backend_web.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
    Long id;                   // Thêm id để khi xác thực Backend trả token có cả thông tin id và username Khách hàng
    String username;
    String role;
}
