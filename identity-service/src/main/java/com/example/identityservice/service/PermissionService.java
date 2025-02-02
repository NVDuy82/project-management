package com.example.identityservice.service;

import com.example.identityservice.dto.request.PermissionCreationRequest;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.enity.Permission;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.mapper.PermissionMapper;
import com.example.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public PermissionResponse getPermission(String permissionId) {
        return permissionMapper.toPermissionResponse(permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED)));
    }

    public PermissionResponse createPermission(PermissionCreationRequest permissionCreationRequest) {
        Permission permission = permissionMapper.toPermission(permissionCreationRequest);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public void deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
