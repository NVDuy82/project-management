package com.example.taskservice.repository.httpclient;

import com.example.taskservice.configuration.AuthenticationRequestInterceptor;
import com.example.taskservice.dto.request.NotificationCreationRequest;
import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${app.services.notification}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface NotificationClient {
    @PostMapping(value = "/internal/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<NotificationResponse> create(@RequestBody NotificationCreationRequest request);
}
