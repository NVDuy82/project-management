package com.example.taskservice.service;

import com.example.taskservice.enity.Task;
import com.example.taskservice.exception.AppException;
import com.example.taskservice.exception.ErrorCode;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.repository.httpclient.ProjectClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    ProjectClient projectClient;
    TaskRepository taskRepository;

    public boolean hasRoleWithProjectId(String projectId, String role) {
        return projectClient.hasRole(projectId, role).getResult();
    }

    public boolean hasRoleWithProjectId(String projectId, String userId, String role) {
        return projectClient.hasRole(projectId, userId, role).getResult();
    }

    public boolean hasRoleWithTaskId(String taskId, String roleName) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        return projectClient.hasRole(task.getProjectId(), roleName).getResult();
    }

    public boolean isAssigned(String taskId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        return task.getAssignedUserId().equals(userId);
    }
}
