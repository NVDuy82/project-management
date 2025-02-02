package com.example.profileservice.mapper;


import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.ProfileUpdateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.enity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);
    
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    void updateProfile(@MappingTarget UserProfile userProfile, ProfileUpdateRequest request);
}
