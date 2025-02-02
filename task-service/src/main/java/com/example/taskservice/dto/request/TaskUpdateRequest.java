package com.example.taskservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateRequest {

    @Size(min = 1, max = 50, message = "INVALID_TASK_NAME")
    String name;
    @Size(max = 100, message = "INVALID_TASK_DESCRIPTION")
    String description;

//    String status;
//    String assignedUserId;
}
