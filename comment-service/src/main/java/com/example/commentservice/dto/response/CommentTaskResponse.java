package com.example.commentservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentTaskResponse {
    String id;
    String content;
    String authorId;
    String taskId;
    Instant timestamp;
}
