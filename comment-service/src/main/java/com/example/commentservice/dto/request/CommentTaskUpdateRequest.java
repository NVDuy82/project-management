package com.example.commentservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentTaskUpdateRequest {
    String content;
    String authorId;
    String taskId;
}
