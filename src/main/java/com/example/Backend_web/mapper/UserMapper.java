package com.example.Backend_web.mapper;

import com.example.Backend_web.dto.request.UserCreationRequest;
import com.example.Backend_web.dto.request.UserUpdateRequest;
import com.example.Backend_web.dto.response.UserResponse;
import com.example.Backend_web.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    //Trả id từng User
    default Long toIdUserResponse(User user) {
        return user.getId();
    }

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
