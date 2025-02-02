package com.example.taskservice.mapper;

import com.example.taskservice.dto.request.AssignRequest;
import com.example.taskservice.dto.request.TaskCreationRequest;
import com.example.taskservice.dto.request.TaskUpdateRequest;
import com.example.taskservice.dto.response.TaskResponse;
import com.example.taskservice.enity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "status", ignore = true)
    Task toTask(TaskCreationRequest taskCreationRequest);

    @Mapping(target = "status", ignore = true)
    void updateTask(@MappingTarget Task task, TaskUpdateRequest taskUpdateRequest);

    void updateTask(@MappingTarget Task task, AssignRequest assignRequest);
    
    TaskResponse toTaskResponse(Task task);
}
