package com.example.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionCreationRequest {
    @Size(min = 1, max = 50, message = "INVALID_PERMISSION_NAME")
    String name;
    @Size(max = 100, message = "INVALID_PERMISSION_DESCRIPTION")
    String description;
}
