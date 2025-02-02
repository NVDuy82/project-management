package com.example.projectservice.controller;

import com.example.projectservice.dto.request.ProjectCreationRequest;
import com.example.projectservice.dto.request.ProjectUpdateRequest;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.service.PermissionService;
import com.example.projectservice.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/permission")
public class InternalPermissionController {
    PermissionService permissionService;

    @GetMapping("/has-role")
    ApiResponse<Boolean> hasRole(@RequestParam String projectId,
                                 @RequestParam String role) {
        return ApiResponse.<Boolean>builder()
                .result(permissionService.hasRole(projectId, role))
                .build();
    }

    @GetMapping("/user-has-role")
    ApiResponse<Boolean> hasRole(@RequestParam String projectId,
                                 @RequestParam String userId,
                                 @RequestParam String role) {
        return ApiResponse.<Boolean>builder()
                .result(permissionService.hasRole(projectId, userId, role))
                .build();
    }
}
