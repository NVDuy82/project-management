package com.example.reportservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    String id;
    String userId;
    String projectId;
    MemberRoleResponse role;
    UserProfileResponse userProfile;
}
