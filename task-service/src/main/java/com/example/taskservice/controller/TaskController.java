package com.example.taskservice.controller;

import com.example.taskservice.dto.request.AssignRequest;
import com.example.taskservice.dto.request.ChangeStatusRequest;
import com.example.taskservice.dto.request.TaskCreationRequest;
import com.example.taskservice.dto.request.TaskUpdateRequest;
import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.PageResponse;
import com.example.taskservice.dto.response.TaskResponse;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;
    
    @GetMapping("/all")
    ApiResponse<List<TaskResponse>> getAllTasks() {
        return ApiResponse.<List<TaskResponse>>builder()
                .result(taskService.getAllTasks())
                .build();
    }
    
    @GetMapping("/my-tasks")
    ApiResponse<PageResponse<TaskResponse>> getAllMyTasks(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<TaskResponse>>builder()
                .result(taskService.getAllMyTasks(page, size))
                .build();
    }

    @GetMapping("/{taskId}")
    ApiResponse<TaskResponse> getTaskById(@PathVariable String taskId) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.getTaskById(taskId))
                .build();
    }


    @GetMapping("/all-in-project")
    ApiResponse<List<TaskResponse>> getAllTasks(@RequestParam String projectId) {
        return ApiResponse.<List<TaskResponse>>builder()
                .result(taskService.getAllTasksByProjectId(projectId))
                .build();
    }

    @GetMapping("/my-tasks-in-project")
    ApiResponse<List<TaskResponse>> getAllMyTasks(@RequestParam String projectId) {
        return ApiResponse.<List<TaskResponse>>builder()
                .result(taskService.getAllMyTasks(projectId))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<TaskResponse> createTask(@RequestBody @Valid TaskCreationRequest taskCreationRequest) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.createTask(taskCreationRequest))
                .build();
    }

    @PutMapping("/{taskId}")
    ApiResponse<TaskResponse> updateTask(@PathVariable String taskId,
                                         @RequestBody @Valid TaskUpdateRequest taskUpdateRequest) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.updateTask(taskId, taskUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/{taskId}")
    ApiResponse<String> deleteProject(@PathVariable String taskId) {
        return ApiResponse.<String>builder()
                .result(taskService.deleteTask(taskId))
                .build();
    }

    @PostMapping("{taskId}/assign")
    ApiResponse<TaskResponse> assign(@PathVariable String taskId, @RequestBody @Valid AssignRequest assignRequest) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.assign(taskId, assignRequest))
                .build();
    }

    @PostMapping("{taskId}/unassign")
    ApiResponse<TaskResponse> assign(@PathVariable String taskId) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.unassign(taskId))
                .build();
    }

    @PostMapping("{taskId}/change-status")
    ApiResponse<TaskResponse> changeStatus(@PathVariable String taskId,
                                           @RequestBody @Valid ChangeStatusRequest changeStatusRequest) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.changeStatus(taskId, changeStatusRequest))
                .build();
    }
}
