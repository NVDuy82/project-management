package com.example.identityservice.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 3, max = 20, message = "INVALID_USERNAME")
    String username;

    @Size(min = 6, max = 30, message = "INVALID_PASSWORD")
    String password;
    
    @Email(message = "INVALID_EMAIL")
    String email;
    
    String fullName;
}
