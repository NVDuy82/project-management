package com.example.identityservice.service;

import com.example.identityservice.dto.request.RoleCreationRequest;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.enity.Permission;
import com.example.identityservice.enity.Role;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.mapper.RoleMapper;
import com.example.identityservice.repository.PermissionRepository;
import com.example.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public RoleResponse getRole(String roleId) {
        return roleMapper.toRoleResponse(roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }

    public RoleResponse createRole(RoleCreationRequest roleCreationRequest) {
        Role role = roleMapper.toRole(roleCreationRequest);

        List<Permission> allPermissionById = permissionRepository.findAllById(roleCreationRequest.getPermissions());
        role.setPermissions(new HashSet<>(allPermissionById));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }
}
