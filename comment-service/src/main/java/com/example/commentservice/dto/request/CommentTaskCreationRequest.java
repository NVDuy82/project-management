package com.example.commentservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentTaskCreationRequest {
    String content;
    String authorId;
    String taskId;
}
