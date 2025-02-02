package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.PermissionCreationRequest;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.enity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permission permission);

    Permission toPermission(PermissionCreationRequest permissionCreationRequest);
}
