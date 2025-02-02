package com.example.taskservice.controller;

import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.TaskResponse;
import com.example.taskservice.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("internal/task")
public class InternalTaskController {
    TaskService taskService;

    @GetMapping("/{taskId}")
    ApiResponse<TaskResponse> getTaskByTaskId(@PathVariable String taskId) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.getTaskById(taskId))
                .build();
    }


    @GetMapping("/all-in-project")
    ApiResponse<List<TaskResponse>> getTaskByProjectId(@RequestParam String projectId) {
        return ApiResponse.<List<TaskResponse>>builder()
                .result(taskService.getAllTasksByProjectId(projectId))
                .build();
    }


    @PostMapping("/delete-all")
    ApiResponse<String> deleteAllByProjectId(@RequestParam String projectId) {
        return ApiResponse.<String>builder()
                .result(taskService.deleteAllByProjectId(projectId))
                .build();
    }
}
