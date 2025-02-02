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
public class ProjectRoleCreationRequest {
    @Size(min = 1, max = 50, message = "INVALID_PROJECT_ROLE_NAME")
    String name;
    @Size(max = 100, message = "INVALID_PROJECT_ROLE_DESCRIPTION")
    String description;
}
