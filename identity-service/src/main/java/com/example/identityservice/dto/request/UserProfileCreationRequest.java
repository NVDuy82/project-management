package com.example.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileCreationRequest {
    String userId;
    String username;
    String email;
    String fullName;
}
