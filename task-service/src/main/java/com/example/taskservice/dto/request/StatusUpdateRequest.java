package com.example.taskservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateRequest {
    @Size(max = 100, message = "INVALID_TASK_STATUS_DESCRIPTION")
    String description;
}
