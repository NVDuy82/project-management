package com.example.notificationservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationResponse {
    String id;
    String fromProjectId;
    String fromTaskId;
    String toUserId;
    String title;
    String body;
    Instant timestamp;
}
