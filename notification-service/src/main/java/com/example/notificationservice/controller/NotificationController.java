package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.NotificationCreationRequest;
import com.example.notificationservice.dto.response.ApiResponse;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.dto.response.PageResponse;
import com.example.notificationservice.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getById(@PathVariable String id) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.get(id))
                .build();
    }

    @GetMapping("/my-notifications")
    public ApiResponse<PageResponse<NotificationResponse>> getMyNotifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
            ) {
        return ApiResponse.<PageResponse<NotificationResponse>>builder()
                .result(notificationService.getMyNotification(page, size))
                .build();
    }

    @GetMapping("/get-notifications")
    public ApiResponse<List<NotificationResponse>> getAllByUserId(@RequestParam String userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getAllByUserId(userId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable String id) {
        return ApiResponse.<String>builder()
                .result(notificationService.delete(id))
                .build();
    }
}
