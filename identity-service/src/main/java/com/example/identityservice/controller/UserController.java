package com.example.identityservice.controller;

import com.example.identityservice.dto.request.ChangePasswordRequest;
import com.example.identityservice.dto.request.ChangeStatusRequest;
import com.example.identityservice.dto.request.UserCreationRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    
    @GetMapping("/all")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }
    
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }
    
    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/username-to-userId")
    ApiResponse<String> getUserIdByUsername(@RequestParam String username) {
        return ApiResponse.<String>builder()
                .result(userService.getUserIdByUsername(username))
                .build();
    }

    @GetMapping("/email-to-userId")
    ApiResponse<String> getUserIdByEmail(@RequestParam String email) {
        return ApiResponse.<String>builder()
                .result(userService.getUserIdByEmail(email))
                .build();
    }
    
    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @PostMapping("/change-password")
    ApiResponse<UserResponse> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.changePassword(changePasswordRequest))
                .build();
    }
    
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @PostMapping("/change-status")
    ApiResponse<UserResponse> changeStatus(@RequestBody ChangeStatusRequest changeStatusRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.changeStatus(changeStatusRequest))
                .build();
    }
    
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        return ApiResponse.<String>builder()
                .result(userService.deleteUser(userId))
                .build();
    }
}
