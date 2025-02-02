package com.example.identityservice.controller;

import com.example.identityservice.dto.request.RoleCreationRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping("/all")
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{roleId}")
    ApiResponse<RoleResponse> getRole(@PathVariable String roleId) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRole(roleId))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreationRequest roleCreationRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleCreationRequest))
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<String> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);

        return ApiResponse.<String>builder()
                .result("Role has been deleted")
                .build();
    }
}
