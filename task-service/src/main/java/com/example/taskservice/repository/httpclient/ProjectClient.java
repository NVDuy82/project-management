package com.example.taskservice.repository.httpclient;

import com.example.taskservice.configuration.AuthenticationRequestInterceptor;
import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.ProjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "project-service", url = "${app.services.project}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProjectClient {
    @GetMapping(value = "/internal/permission/has-role", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> hasRole(@RequestParam String projectId,
                                 @RequestParam String role);


    @GetMapping(value = "/internal/permission/user-has-role", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> hasRole(@RequestParam String projectId,
                                 @RequestParam String userId,
                                 @RequestParam String role);

    @GetMapping(value = "/internal/project/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProjectResponse> getById(@PathVariable String projectId);
}
