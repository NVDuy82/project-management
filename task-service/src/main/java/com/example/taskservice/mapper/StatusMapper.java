package com.example.taskservice.mapper;

import com.example.taskservice.dto.request.StatusCreationRequest;
import com.example.taskservice.dto.request.StatusUpdateRequest;
import com.example.taskservice.dto.response.TaskStatusResponse;
import com.example.taskservice.enity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    Status toStatus(StatusCreationRequest statusCreationRequest);
    
    void updateStatus(@MappingTarget Status status, StatusUpdateRequest statusUpdateRequest);
    
    TaskStatusResponse toStatusResponse(Status status);
}
