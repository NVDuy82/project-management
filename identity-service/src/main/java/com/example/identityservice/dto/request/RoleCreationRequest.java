package com.example.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreationRequest {
    @Size(min = 1, max = 50, message = "INVALID_ROLE_NAME")
    String name;
    @Size(max = 100, message = "INVALID_ROLE_DESCRIPTION")
    String description;
    Set<String> permissions;
}
