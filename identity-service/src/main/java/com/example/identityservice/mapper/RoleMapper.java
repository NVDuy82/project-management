package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.RoleCreationRequest;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.enity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest roleCreationRequest);
}
