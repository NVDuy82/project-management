package com.example.projectservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreationRequest {
    @Size(min = 1, max = 50, message = "INVALID_PROJECT_NAME")
    String name;
    @Size(max = 100, message = "INVALID_PROJECT_DESCRIPTION")
    String description;
}
