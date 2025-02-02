package com.example.reportservice.dto.response;

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
    String name;
    String description;
    Instant createAt;
    UserProfileResponse assignedUserProfile;
    TaskStatusResponse status;
}
