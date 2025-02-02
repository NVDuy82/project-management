package com.example.notificationservice.mapper;


import com.example.notificationservice.dto.request.NotificationCreationRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.enity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationCreationRequest request);
    
    NotificationResponse toNotificationResponse(Notification notification);
}
