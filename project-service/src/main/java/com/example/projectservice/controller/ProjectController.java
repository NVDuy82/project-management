package com.example.projectservice.controller;

import com.example.projectservice.dto.request.*;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.PageResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {
    ProjectService projectService;
    
    @GetMapping("/all")
    ApiResponse<List<ProjectResponse>> getAllProjects() {
        return ApiResponse.<List<ProjectResponse>>builder()
                .result(projectService.getAllProjects())
                .build();
    }
    
    @GetMapping("/my-projects")
    ApiResponse<PageResponse<ProjectResponse>> getMyProjects(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
            ) {
        return ApiResponse.<PageResponse<ProjectResponse>>builder()
                .result(projectService.getMyProjects(page, size))
                .build();
    }
    
    @PostMapping("/create")
    ApiResponse<ProjectResponse> createProject(@RequestBody @Valid ProjectCreationRequest projectCreationRequest) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.createProject(projectCreationRequest))
                .build();
    }

    @GetMapping("/{projectId}")
    ApiResponse<ProjectResponse> getById(@PathVariable String projectId) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.getById(projectId))
                .build();
    }
    
    @PutMapping("/{projectId}")
    ApiResponse<ProjectResponse> updateProject(@PathVariable String projectId,
                                         @RequestBody @Valid ProjectUpdateRequest projectUpdateRequest) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.updateProject(projectId, projectUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/{projectId}")
    ApiResponse<String> deleteProject(@PathVariable String projectId) {
        return ApiResponse.<String>builder()
                .result(projectService.deleteProject(projectId))
                .build();
    }
}
