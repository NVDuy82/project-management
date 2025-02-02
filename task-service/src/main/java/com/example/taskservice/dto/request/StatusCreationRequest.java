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
public class StatusCreationRequest {

    @Size(min = 1, max = 50, message = "INVALID_TASK_STATUS_NAME")
    String name;
    @Size(max = 100, message = "INVALID_TASK_STATUS_DESCRIPTION")
    String description;
}
