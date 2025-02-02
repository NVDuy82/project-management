package com.example.identityservice.controller;


import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.service.AuthenticationService;
import com.example.identityservice.service.CheckService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/check")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalCheckController {
    CheckService checkService;

    @GetMapping("/exist-user")
    ApiResponse<Boolean> existUser(@RequestParam String userId) {
        return ApiResponse.<Boolean>builder()
                .result(checkService.existUser(userId))
                .build();
    }
}
