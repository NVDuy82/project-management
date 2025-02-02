package com.example.identityservice.controller;


import com.example.identityservice.dto.request.AuthenticationRequest;
import com.example.identityservice.dto.request.IntrospectRequest;
import com.example.identityservice.dto.request.LogoutRequest;
import com.example.identityservice.dto.request.RefreshTokenRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.IntrospectResponse;
import com.example.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(introspectRequest))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody @Valid LogoutRequest logoutRequest) throws ParseException, JOSEException {
        return ApiResponse.<String>builder()
                .result(authenticationService.logout(logoutRequest))
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(refreshTokenRequest))
                .build();
    }
}
