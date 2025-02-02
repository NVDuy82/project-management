package com.example.identityservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    String password;

    @Size(min = 6, max = 30, message = "INVALID_PASSWORD")
    String newPassword;
}
