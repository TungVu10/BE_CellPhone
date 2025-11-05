package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.UserCreationRequest;
import com.example.Backend_web.dto.response.UserResponse;
import com.example.Backend_web.entity.User;
import com.example.Backend_web.enums.Role;
import com.example.Backend_web.mapper.UserMapper;
import com.example.Backend_web.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    // ✅ Thêm tài khoản người dùng mới (mặc định role USER)
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username da ton tai!");
        }

        User user = userMapper.toUser(request);

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Gán role mặc định là USER
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("In method get users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

}
