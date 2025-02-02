package com.example.reportservice.controller;

import com.example.reportservice.dto.response.ApiResponse;
import com.example.reportservice.dto.response.ReportProjectResponse;
import com.example.reportservice.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/report-project")
    ApiResponse<ReportProjectResponse> getAllMyTasks(@RequestParam String projectId) {
        return ApiResponse.<ReportProjectResponse>builder()
                .result(reportService.getReportProject(projectId))
                .build();
    }
}
