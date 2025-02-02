package com.example.profileservice.controller;

import com.example.profileservice.dto.request.ProfileUpdateRequest;
import com.example.profileservice.dto.response.ApiResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;
    
    @GetMapping("/users/{profileId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String profileId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfile(profileId))
                .build();
    }
    
    @GetMapping("/users/all")
    ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles())
                .build();
    }
    
    @GetMapping("/users/my-profile")
    ApiResponse<UserProfileResponse> getMyProfile() {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getMyProfile())
                .build();
    }

    @GetMapping("/users/get-by-user-id/{userId}")
    ApiResponse<UserProfileResponse> getProfileByUserId(@PathVariable String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUserId(userId))
                .build();
    }

    @PutMapping("/users/my-profile")
    ApiResponse<UserProfileResponse> updateProfile(@RequestBody @Valid ProfileUpdateRequest profileUpdateRequest) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateProfile(profileUpdateRequest))
                .build();
    }

    @PostMapping("/search")
    ApiResponse<List<UserProfileResponse>> search(@RequestParam String query) {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.search(query))
                .build();
    }
}
