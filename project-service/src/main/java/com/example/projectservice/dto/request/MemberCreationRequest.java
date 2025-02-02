package com.example.projectservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreationRequest {
    String projectId;
    String userId;
    String role;
}
