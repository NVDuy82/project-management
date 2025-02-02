package com.example.historyservice.repository.httpclient;

import com.example.historyservice.configuration.AuthenticationRequestInterceptor;
import com.example.historyservice.dto.response.ApiResponse;
import com.example.historyservice.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @GetMapping(value = "/internal/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
