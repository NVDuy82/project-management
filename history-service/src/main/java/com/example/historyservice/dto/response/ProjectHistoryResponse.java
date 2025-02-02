package com.example.historyservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectHistoryResponse {
    String id;
    String projectId;
    String content;
    ProjectChangeTypeResponse type;
    Instant timestamp;
}
