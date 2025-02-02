package com.example.taskservice.service;

import com.example.taskservice.contant.PredefinedRole;
import com.example.taskservice.dto.request.*;
import com.example.taskservice.dto.response.ApiResponse;
import com.example.taskservice.dto.response.PageResponse;
import com.example.taskservice.dto.response.TaskResponse;
import com.example.taskservice.dto.response.UserProfileResponse;
import com.example.taskservice.enity.Task;
import com.example.taskservice.enity.Status;
import com.example.taskservice.exception.AppException;
import com.example.taskservice.exception.ErrorCode;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.repository.StatusRepository;
import com.example.taskservice.repository.httpclient.NotificationClient;
import com.example.taskservice.repository.httpclient.ProfileClient;
import com.example.taskservice.repository.httpclient.ProjectClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TaskService {
    PermissionService permissionService;
    TaskRepository taskRepository;
    StatusRepository statusRepository;
    TaskMapper taskMapper;
    ProfileClient profileClient;
    ProjectClient projectClient;
    NotificationClient notificationClient;

    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskResponse> getAllTasks() {
        List<Task> all = taskRepository.findAll();
        
        return all.stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @PreAuthorize("@permissionService.hasRoleWithProjectId(#projectId, '*')")
    public List<TaskResponse> getAllTasksByProjectId(String projectId) {
        List<Task> all = taskRepository.findAllByProjectId(projectId);

        return all.stream()
                .map(this::mapAndUpdateProfile)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public PageResponse<TaskResponse> getAllMyTasks(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Task> pageData = taskRepository.findAllByAssignedUserId(userId, pageable);

        return PageResponse.<TaskResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(this::mapAndUpdateProfile).toList())
                .build();
    }

    @PreAuthorize("@permissionService.hasRoleWithProjectId(#projectId, '*')")
    public List<TaskResponse> getAllMyTasks(String projectId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Task> allByAssignedUserId = taskRepository.findAllByAssignedUserIdAndProjectId(userId, projectId);

        return allByAssignedUserId.stream()
                .map(this::mapAndUpdateProfile)
                .toList();
    }

    @PreAuthorize("@permissionService.hasRoleWithTaskId(#taskId, '*')")
    public TaskResponse getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        return mapAndUpdateProfile(task);
    }

    @PreAuthorize("@permissionService.hasRoleWithProjectId(#taskCreationRequest.getProjectId(), " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MANAGER)")
    public TaskResponse createTask(TaskCreationRequest taskCreationRequest) {
        Task task = taskMapper.toTask(taskCreationRequest);

        if (taskCreationRequest.getAssignedUserId() != null &&
                !permissionService.hasRoleWithProjectId(
                        task.getProjectId(),
                        taskCreationRequest.getAssignedUserId(),
                        PredefinedRole.ROLE_MEMBER)) {
            throw new AppException(ErrorCode.MUST_BE_MEMBER);
        }

        Status status = statusRepository.findById(taskCreationRequest.getStatus())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_STATUS_NOT_EXISTED));

        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        task.setStatus(status);

        task = taskRepository.save(task);
        String projectName = projectClient.getById(task.getProjectId()).getResult().getName();
        if (task.getAssignedUserId() != null) {
            notificationClient.create(NotificationCreationRequest.builder()
                    .fromProjectId(task.getProjectId())
                    .fromTaskId(task.getId())
                    .toUserId(task.getAssignedUserId())
                    .title("[" + projectName + "] Được phân công công việc")
                    .body("Bạn vừa được phân công công việc " + task.getName())
                    .build()
            );
        }

        return mapAndUpdateProfile(task);
    }

    @PreAuthorize("@permissionService.isAssigned(#taskId) && " +
            "@permissionService.hasRoleWithTaskId(#taskId, " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MEMBER)")
    public TaskResponse updateTask(String taskId, TaskUpdateRequest taskUpdateRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        taskMapper.updateTask(task, taskUpdateRequest);
        task.setUpdatedAt(Instant.now());
        task = taskRepository.save(task);

        return mapAndUpdateProfile(task);
    }

    @PreAuthorize("@permissionService.hasRoleWithTaskId(#taskId, " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MANAGER)")
    public String deleteTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        if (task.getCreatedAt().plus(1, ChronoUnit.HOURS).isAfter(Instant.now())) {
            taskRepository.delete(task);
        } else {
            throw new AppException(ErrorCode.CANT_DELETE_TASK);
        }

        return "Tasks have been deleted";
    }

    public String deleteAllByProjectId(String projectId) {
        List<Task> allByProjectId = taskRepository.findAllByProjectId(projectId);

        taskRepository.deleteAll(allByProjectId);
        return "Tasks have been deleted";
    }

    @PreAuthorize("@permissionService.hasRoleWithTaskId(#taskId, " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MANAGER)")
    public TaskResponse assign(String taskId, AssignRequest assignRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        if (!permissionService.hasRoleWithProjectId(task.getProjectId(),
                assignRequest.getAssignedUserId(), PredefinedRole.ROLE_MEMBER)) {
            throw new AppException(ErrorCode.MUST_BE_MEMBER);
        }

        if (task.getAssignedUserId() != null &&
                task.getAssignedUserId().equals(assignRequest.getAssignedUserId())) {
            throw new AppException(ErrorCode.BAD_ASSIGNED);
        }

        unassign(taskId);
        taskMapper.updateTask(task, assignRequest);

        task = taskRepository.save(task);
        String projectName = projectClient.getById(task.getProjectId()).getResult().getName();
        notificationClient.create(NotificationCreationRequest.builder()
                .fromProjectId(task.getProjectId())
                .fromTaskId(task.getId())
                .toUserId(task.getAssignedUserId())
                .title("[" + projectName + "] Được phân công công việc")
                .body("Bạn vừa được phân công công việc " + task.getName())
                .build()
        );

        return mapAndUpdateProfile(task);
    }

    @PreAuthorize("@permissionService.hasRoleWithTaskId(#taskId, " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MANAGER)")
    public TaskResponse unassign(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        if (task.getAssignedUserId() != null) {
            String userId = task.getAssignedUserId();
            task.setAssignedUserId(null);
            task = taskRepository.save(task);

            String projectName = projectClient.getById(task.getProjectId()).getResult().getName();
            notificationClient.create(NotificationCreationRequest.builder()
                    .fromProjectId(task.getProjectId())
                    .fromTaskId(task.getId())
                    .toUserId(userId)
                    .title("[" + projectName + "] Gỡ phụ trách công việc")
                    .body("Bạn vừa bị ngừng phụ trách công việc " + task.getName())
                    .build()
            );
        }

        return mapAndUpdateProfile(task);
    }

    @PreAuthorize("@permissionService.isAssigned(#taskId) && " +
            "@permissionService.hasRoleWithTaskId(#taskId, " +
            "T(com.example.taskservice.contant.PredefinedRole).ROLE_MEMBER)")
    public TaskResponse changeStatus(String taskId, ChangeStatusRequest changeStatusRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        task.setStatus(statusRepository.findById(changeStatusRequest.getStatus())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_STATUS_NOT_EXISTED)));

        task.setUpdatedAt(Instant.now());
        task = taskRepository.save(task);
        if (task.getAssignedUserId() != null && !task.getStatus().getName().equals(changeStatusRequest.getStatus())) {
            String projectName = projectClient.getById(task.getProjectId()).getResult().getName();
            notificationClient.create(NotificationCreationRequest.builder()
                    .fromProjectId(task.getProjectId())
                    .fromTaskId(task.getId())
                    .toUserId(task.getAssignedUserId())
                    .title("[" + projectName + "] Thay đổi trạng thái công việc")
                    .body("Công việc " + task.getName() + " chuyển sang trạng thái " + task.getStatus().getName())
                    .build()
            );
        }

        return mapAndUpdateProfile(task);
    }

    private TaskResponse mapAndUpdateProfile(Task task) {
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        taskResponse.setAssignedUserProfile(getProfile(task.getAssignedUserId()));

        try {
            taskResponse.setProjectName(projectClient.getById(task.getProjectId()).getResult().getName());
        } catch (Exception e) {

        }

        return taskResponse;
    }

    private UserProfileResponse getProfile(String userId) {
        try {
            ApiResponse<UserProfileResponse> response = profileClient.getProfile(userId);

            if (response.getCode() != 1000) return null;

            return response.getResult();
        } catch (FeignException exception) {
            return null;
        }
    }
}
