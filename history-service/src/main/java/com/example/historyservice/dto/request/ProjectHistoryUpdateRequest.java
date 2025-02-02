package com.example.historyservice.dto.request;

import com.example.historyservice.dto.response.ProjectChangeTypeResponse;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectHistoryUpdateRequest {
    String projectId;
    String content;
    ProjectChangeTypeResponse type;
    Instant timestamp;
}
