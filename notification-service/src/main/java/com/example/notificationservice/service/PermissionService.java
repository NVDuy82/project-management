package com.example.notificationservice.service;

import com.example.notificationservice.dto.request.NotificationCreationRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.enity.Notification;
import com.example.notificationservice.exception.AppException;
import com.example.notificationservice.exception.ErrorCode;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    NotificationRepository notificationRepository;

    public boolean isOwner(String notificationId, String userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        return notification.getToUserId().equals(userId);
    }
}
