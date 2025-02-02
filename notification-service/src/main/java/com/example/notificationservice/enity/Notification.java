package com.example.notificationservice.enity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(value = "notification")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @MongoId
    String id;

    String fromProjectId;
    String fromTaskId;
    String toUserId;

    String title;
    String body;

    Instant timestamp;
}
