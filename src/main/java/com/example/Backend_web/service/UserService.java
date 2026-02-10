package com.example.Backend_web.service;

import com.example.Backend_web.configuration.SecurityUtil;
import com.example.Backend_web.dto.request.UserCreationRequest;
import com.example.Backend_web.dto.request.UserUpdateRequest;
import com.example.Backend_web.dto.response.OrderResponse;
import com.example.Backend_web.dto.response.UserResponse;
import com.example.Backend_web.entity.Order;
import com.example.Backend_web.entity.User;
import com.example.Backend_web.enums.OrderStatus;
import com.example.Backend_web.enums.Role;
import com.example.Backend_web.mapper.OrderMapper;
import com.example.Backend_web.mapper.OrderMapperImpl;
import com.example.Backend_web.mapper.UserMapper;
import com.example.Backend_web.repository.OrderRepository;
import com.example.Backend_web.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final OrderRepository orderRepository;
    private final OrderMapperImpl orderMapperImpl;
    private final OrderMapper orderMapper;

    // Thêm tài khoản người dùng mới (mặc định role USER)
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

    //Lấy danh sách thông tin Users
    @PreAuthorize("hasRole('ADMIN')")
//    public List<UserResponse> getUsers(){
//        log.info("In method get users");
//        return userRepository.findAll().stream()
//                .map(userMapper::toUserResponse).toList();
//    }
    public List<UserResponse> getUsers() {
         List<User> users = userRepository.findByRoles(Role.USER);
         return users.stream()
                 .map(userMapper::toUserResponse)
                 .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    //Hủy đơn hàng

    //Khách hàng xem thông tin của mình
    public UserResponse getMyProfile() {
        String userName = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Khong tim thay User"));
        return userMapper.toUserResponse(user);
    }

    //Khách hàng xem danh sách các đơn hàng của mình
    public List<OrderResponse> getMyOrders() {
        String userName = SecurityUtil.getCurrentUsername();

        return orderRepository.findByUser_Username(userName)
                .stream().map(orderMapper::toResponse)
                .toList();
    }

    //Khách hàng xem chi tiết đơn hàng của mình
    public OrderResponse getMyOrderDetail(Long orderId) {
        String userName = SecurityUtil.getCurrentUsername();
        Order order = orderRepository.findByIdAndUser_Username(orderId, userName)
                .orElseThrow(() -> new RuntimeException("Khong tim thay Order"));
        return orderMapper.toResponse(order);
    }

    //Khách hàng hủy đơn hàng của mình (trong trường hợp đơn hàng trước khi bắt đầu được giao hàng)
    @Transactional
    public void cancelOrder(Long orderId){
        String userName = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Khong tim thay User"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Don hang khong ton tai"));

        //Check không cho hủy đơn hàng của User khác
        if(!order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Khong the huy don hang cua User khac");
        }
        //Check trạng thái đơn hàng phải ở trạng thái PENDING thì mới được hủy đơn hàng
        if(order.getStatus()!= OrderStatus.PENDING){
            throw new RuntimeException("Chi huy duoc don hang o trang thai PENDING");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    //Khách hàng sửa thông tin của mình
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay User"));

        user.setFullName(request.getFullName());
        //user.setFullname(request.getFullName());
        //user.setPassword(request.getPassword());
        //Update Password là phải encode (mã hóa mật khẩu) sau đó mới lưu vào DB
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
        user.setEmail(request.getEmail());

        User updateUser = userRepository.save(user);
        return userMapper.toUserResponse(updateUser);
    }

}
