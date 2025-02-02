package com.example.projectservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    String id;
    String name;
    String description;
    Instant createdAt;
    Instant updatedAt;
    UserProfileResponse createdBy;

    ProjectStatusResponse status;
    ProjectRoleResponse myRole;

//    Set<MemberResponse> members;
}
