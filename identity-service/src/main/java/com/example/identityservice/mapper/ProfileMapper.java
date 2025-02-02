package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.UserProfileCreationRequest;
import com.example.identityservice.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    UserProfileCreationRequest toProfileCreationRequest(UserCreationRequest userCreationRequest);
}
