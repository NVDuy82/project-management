package com.example.gatewayservice.service;

import com.example.gatewayservice.dto.request.IntrospectRequest;
import com.example.gatewayservice.dto.response.ApiResponse;
import com.example.gatewayservice.dto.response.IntrospectResponse;
import com.example.gatewayservice.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;
    
    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}
