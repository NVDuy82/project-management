package com.example.taskservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    String id;
    String projectId;
    String projectName;
    String name;
    String description;
    Instant createdAt;
    UserProfileResponse assignedUserProfile;
    TaskStatusResponse status;
}
