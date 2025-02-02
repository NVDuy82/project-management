package com.example.historyservice.mapper;

import com.example.historyservice.dto.request.ProjectChangeTypeCreationRequest;
import com.example.historyservice.dto.request.ProjectChangeTypeUpdateRequest;
import com.example.historyservice.dto.response.ProjectChangeTypeResponse;
import com.example.historyservice.enity.ProjectChangeType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectChangeTypeMapper {
    ProjectChangeType toProjectChangeType(ProjectChangeTypeCreationRequest projectChangeTypeCreationRequest);
    
    void updateProjectChangeType(@MappingTarget ProjectChangeType projectChangeType,
                                 ProjectChangeTypeUpdateRequest projectChangeTypeUpdateRequest);

    ProjectChangeTypeResponse toProjectChangeTypeResponse(ProjectChangeType projectChangeType);
}
