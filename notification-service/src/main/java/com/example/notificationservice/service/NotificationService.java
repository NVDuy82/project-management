package com.example.notificationservice.service;

import com.example.notificationservice.dto.request.NotificationCreationRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.dto.response.PageResponse;
import com.example.notificationservice.enity.Notification;
import com.example.notificationservice.exception.AppException;
import com.example.notificationservice.exception.ErrorCode;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    PermissionService permissionService;

    public NotificationResponse create(NotificationCreationRequest notificationCreationRequest) {
        Notification notification = notificationMapper.toNotification(notificationCreationRequest);
        notification.setTimestamp(Instant.now());
        notification = notificationRepository.save(notification);

        return notificationMapper.toNotificationResponse(notification);
    }

    @PreAuthorize("@permissionService.isOwner(id, authentication.name) || hasRole('ADMIN')")
    public NotificationResponse get(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        return notificationMapper.toNotificationResponse(notification);
    }

    @PreAuthorize("hasRole('USER')")
    public PageResponse<NotificationResponse> getMyNotification(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Notification> pageData = notificationRepository.findAllByToUserId(userId, pageable);

        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(notificationMapper::toNotificationResponse).toList())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') || authentication.name == #userId")
    public List<NotificationResponse> getAllByUserId(String userId) {
        List<Notification> list = notificationRepository.findAllByToUserId(userId);

        return list.stream().map(notificationMapper::toNotificationResponse).toList();
    }

    @PreAuthorize("@permissionService.isOwner(id, authentication.name)")
    public String delete(String id) {
        notificationRepository.deleteById(id);

        return "Notification has been deleted";
    }

}
