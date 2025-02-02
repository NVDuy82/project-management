package com.example.projectservice.controller;

import com.example.projectservice.dto.request.ProjectRoleCreationRequest;
import com.example.projectservice.dto.request.ProjectRoleUpdateRequest;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.ProjectRoleResponse;
import com.example.projectservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;
    
    @GetMapping("/all")
    ApiResponse<List<ProjectRoleResponse>> getAllRoles() {
        return ApiResponse.<List<ProjectRoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }
    
    @PostMapping("/create")
    ApiResponse<ProjectRoleResponse> createRole(@RequestBody @Valid ProjectRoleCreationRequest projectRoleCreationRequest) {
        return ApiResponse.<ProjectRoleResponse>builder()
                .result(roleService.createRole(projectRoleCreationRequest))
                .build();
    }
    
    @PutMapping("/{roleId}")
    ApiResponse<ProjectRoleResponse> updateRole(@PathVariable String roleId,
                                                @RequestBody @Valid ProjectRoleUpdateRequest projectRoleUpdateRequest) {
        return ApiResponse.<ProjectRoleResponse>builder()
                .result(roleService.updateRole(roleId, projectRoleUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/{roleId}")
    ApiResponse<String> deleteRole(@PathVariable String roleId) {
        return ApiResponse.<String>builder()
                .result(roleService.deleteRole(roleId))
                .build();
    }
}
