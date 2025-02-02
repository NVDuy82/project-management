package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.NotificationCreationRequest;
import com.example.notificationservice.dto.response.ApiResponse;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal")
public class InternalNotificationController {
    NotificationService notificationService;
    
    @PostMapping("/create")
    ApiResponse<NotificationResponse> create(@RequestBody @Valid NotificationCreationRequest request) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.create(request))
                .build();
    }
}
