package com.example.taskservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreationRequest {
    String fromProjectId;
    String fromTaskId;
    String toUserId;

    String title;
    String body;
}
