package com.example.reportservice.repository.httpclient;

import com.example.reportservice.configuration.AuthenticationRequestInterceptor;
import com.example.reportservice.dto.response.ApiResponse;
import com.example.reportservice.dto.response.ProjectResponse;
import com.example.reportservice.dto.response.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "task-service", url = "${app.services.task}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface TaskClient {
    @GetMapping(value = "/internal/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<TaskResponse> getTaskById(@PathVariable String taskId);

    @GetMapping(value = "/internal/task/all-in-project", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<TaskResponse>> getTasksByProjectId(@RequestParam String projectId);
}
