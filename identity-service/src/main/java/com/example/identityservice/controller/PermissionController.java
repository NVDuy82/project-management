package com.example.identityservice.controller;

import com.example.identityservice.dto.request.PermissionCreationRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping("/all")
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }

    @GetMapping("/{permissionId}")
    ApiResponse<PermissionResponse> getPermission(@PathVariable("permissionId") String permissionId) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.getPermission(permissionId))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionCreationRequest permissionCreationRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionCreationRequest))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<String> deletePermission(@PathVariable String permissionId) {
        permissionService.deletePermission(permissionId);

        return ApiResponse.<String>builder()
                .result("Permission has been deleted")
                .build();
    }
}
