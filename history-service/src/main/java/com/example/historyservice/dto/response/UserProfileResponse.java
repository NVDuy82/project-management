package com.example.historyservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserProfileResponse {
    String id;
    String userId;
    String username;
    String email;
    String fullName;
}
