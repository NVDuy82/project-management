package com.example.projectservice.mapper;

import com.example.projectservice.dto.request.ProjectStatusCreationRequest;
import com.example.projectservice.dto.request.ProjectStatusUpdateRequest;
import com.example.projectservice.dto.response.ProjectStatusResponse;
import com.example.projectservice.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    Status toStatus(ProjectStatusCreationRequest projectStatusCreationRequest);
    
    void updateStatus(@MappingTarget Status status, ProjectStatusUpdateRequest projectStatusUpdateRequest);
    
    ProjectStatusResponse toStatusResponse(Status status);
}
