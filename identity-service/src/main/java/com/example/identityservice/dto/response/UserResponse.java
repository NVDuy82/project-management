package com.example.identityservice.dto.response;

import com.example.identityservice.enity.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String id;
    String username;
//    String passwordHash;
    String email;
    Instant createAt;
    Status status;
    Set<RoleResponse> roles;
}
