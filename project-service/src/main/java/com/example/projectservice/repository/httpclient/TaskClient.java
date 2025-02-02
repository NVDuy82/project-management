package com.example.projectservice.repository.httpclient;

import com.example.projectservice.configuration.AuthenticationRequestInterceptor;
import com.example.projectservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "task-service", url = "${app.services.task}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface TaskClient {
    @PostMapping(value = "/internal/task/delete-all", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> deleteAllByProjectId(@RequestParam String projectId);
}
