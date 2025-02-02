package com.example.reportservice.repository.httpclient;

import com.example.reportservice.configuration.AuthenticationRequestInterceptor;
import com.example.reportservice.dto.response.ApiResponse;
import com.example.reportservice.dto.response.MemberResponse;
import com.example.reportservice.dto.response.ProjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "project-service", url = "${app.services.project}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProjectClient {
    @GetMapping(value = "/internal/project/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProjectResponse> getProjectById(@PathVariable String projectId);

    @GetMapping(value = "/internal/member/get-members", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<MemberResponse>> getMembers(@RequestParam String projectId);
}
