package com.example.profileservice.controller;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.response.ApiResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;
    
    
    @PostMapping("/internal/users")
    ApiResponse<UserProfileResponse> createProfile(@RequestBody @Valid ProfileCreationRequest profileCreationRequest) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createProfile(profileCreationRequest))
                .build();
    }
    
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getByUserId(userId))
                .build();
    }
}
