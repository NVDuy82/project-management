package com.example.historyservice.dto.request;

import com.example.historyservice.dto.response.ProjectChangeTypeResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectHistoryCreationRequest {
    String projectId;
    String content;
    ProjectChangeTypeResponse type;
}
