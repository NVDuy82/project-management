package com.example.projectservice.service;

import com.example.projectservice.dto.request.ProjectRoleCreationRequest;
import com.example.projectservice.dto.request.ProjectRoleUpdateRequest;
import com.example.projectservice.dto.response.ProjectRoleResponse;
import com.example.projectservice.entity.Role;
import com.example.projectservice.exception.AppException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.mapper.RoleMapper;
import com.example.projectservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    
    public List<ProjectRoleResponse> getAllRoles() {
        List<Role> all = roleRepository.findAll();
        
        return all.stream().map(roleMapper::toRoleResponse).toList();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectRoleResponse createRole(ProjectRoleCreationRequest projectRoleCreationRequest) {
        Role role = roleMapper.toRole(projectRoleCreationRequest);
        role = roleRepository.save(role);
        
        return roleMapper.toRoleResponse(role);
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectRoleResponse updateRole(String roleId, ProjectRoleUpdateRequest projectRoleUpdateRequest) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        
        roleMapper.updateRole(role, projectRoleUpdateRequest);
        role = roleRepository.save(role);
        
        return roleMapper.toRoleResponse(role);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
        
        return "Role has been deleted";
    }
}
