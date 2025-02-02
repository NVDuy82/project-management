package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.UserCreationRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.enity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);
    
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
