package com.example.reportservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ManagementInformationProjectResponse {
    List<String> managerIds;
    UserProfileResponse createdBy;
}
