package com.example.reportservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BasicInformationProjectResponse {
    String projectId;
    String name;
    String description;
    Instant createdAt;
    Instant updatedAt;
    ProjectStatusResponse status;
}
