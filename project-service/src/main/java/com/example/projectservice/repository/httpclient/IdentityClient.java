package com.example.projectservice.repository.httpclient;

import com.example.projectservice.configuration.AuthenticationRequestInterceptor;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "identity-service", url = "${app.services.identity}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface IdentityClient {
    @GetMapping(value = "/internal/users/exist-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> existById(@RequestParam String userId);
}
