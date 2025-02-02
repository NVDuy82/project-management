package com.example.historyservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectChangeTypeUpdateRequest {
    String name;
    String description;
}
