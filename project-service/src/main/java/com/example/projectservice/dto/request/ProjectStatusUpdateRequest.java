package com.example.projectservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStatusUpdateRequest {
    @Size(max = 100, message = "INVALID_PROJECT_STATUS_DESCRIPTION")
    String description;
}
