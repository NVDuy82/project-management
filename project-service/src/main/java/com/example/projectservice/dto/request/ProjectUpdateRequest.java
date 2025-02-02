package com.example.projectservice.dto.request;

import com.example.projectservice.entity.Member;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateRequest {
    @Size(min = 1, max = 50, message = "INVALID_PROJECT_NAME")
    String name;
    @Size(max = 100, message = "INVALID_PROJECT_DESCRIPTION")
    String description;
}
