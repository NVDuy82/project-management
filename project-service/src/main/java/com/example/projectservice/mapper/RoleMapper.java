package com.example.projectservice.mapper;

import com.example.projectservice.dto.request.ProjectRoleCreationRequest;
import com.example.projectservice.dto.request.ProjectRoleUpdateRequest;
import com.example.projectservice.dto.response.ProjectRoleResponse;
import com.example.projectservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(ProjectRoleCreationRequest projectRoleCreationRequest);
    
    void updateRole(@MappingTarget Role role, ProjectRoleUpdateRequest projectRoleUpdateRequest);
    
    ProjectRoleResponse toRoleResponse(Role role);
}
