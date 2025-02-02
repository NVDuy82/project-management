package com.example.projectservice.controller;

import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/project")
public class InternalProjectController {
    ProjectService projectService;

    @GetMapping("/{projectId}")
    ApiResponse<ProjectResponse> getById(@PathVariable String projectId) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.getById(projectId))
                .build();
    }
}
