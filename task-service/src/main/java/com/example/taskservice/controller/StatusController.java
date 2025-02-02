package com.example.taskservice.controller;

import com.example.taskservice.dto.request.StatusCreationRequest;
import com.example.taskservice.dto.request.StatusUpdateRequest;
import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.TaskStatusResponse;
import com.example.taskservice.service.StatusService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {
    StatusService statusService;
    
    @GetMapping("/status/all")
    ApiResponse<List<TaskStatusResponse>> getAllStatuses() {
        return ApiResponse.<List<TaskStatusResponse>>builder()
                .result(statusService.getAllStatuses())
                .build();
    }
    
    @PostMapping("/status/create")
    ApiResponse<TaskStatusResponse> createStatus(@RequestBody @Valid StatusCreationRequest statusCreationRequest) {
        return ApiResponse.<TaskStatusResponse>builder()
                .result(statusService.createStatus(statusCreationRequest))
                .build();
    }
    
    @PutMapping("/status/{roleId}")
    ApiResponse<TaskStatusResponse> updateStatus(@PathVariable String roleId,
                                                 @RequestBody @Valid StatusUpdateRequest statusUpdateRequest) {
        return ApiResponse.<TaskStatusResponse>builder()
                .result(statusService.updateStatus(roleId, statusUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/status/{roleId}")
    ApiResponse<String> deleteStatus(@PathVariable String roleId) {
        return ApiResponse.<String>builder()
                .result(statusService.deleteStatus(roleId))
                .build();
    }
}
