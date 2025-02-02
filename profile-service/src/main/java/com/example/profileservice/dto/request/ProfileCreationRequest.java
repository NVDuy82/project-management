package com.example.profileservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationRequest {
    String userId;
    String username;
    String email;

    @Size(min = 1, max = 50, message = "INVALID_FULL_NAME")
    String fullName;
}
