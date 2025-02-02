package com.example.commentservice.service;

import com.example.commentservice.dto.request.CommentTaskCreationRequest;
import com.example.commentservice.dto.request.CommentTaskUpdateRequest;
import com.example.commentservice.dto.response.ApiResponse;
import com.example.commentservice.dto.response.CommentTaskResponse;
import com.example.commentservice.dto.response.UserProfileResponse;
import com.example.commentservice.enity.CommentTask;
import com.example.commentservice.mapper.CommentTaskMapper;
import com.example.commentservice.repository.CommentTaskRepository;
import com.example.commentservice.repository.httpclient.ProfileClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentTaskService {
    CommentTaskRepository commentTaskRepository;
    CommentTaskMapper commentTaskMapper;
    ProfileClient profileClient;

    @PreAuthorize("hasRole('ADMIN')")
    public List<CommentTaskResponse> getAllCommentTasks() {
        List<CommentTask> all = commentTaskRepository.findAll();
        
        return all.stream()
                .map(commentTaskMapper::toTaskResponse)
                .toList();
    }


    public List<CommentTaskResponse> getAllCommentTasksByTaskId(String taskId) {
        List<CommentTask> all = commentTaskRepository.findAllByTaskId(taskId);

        return all.stream()
                .map(commentTaskMapper::toTaskResponse)
                .toList();
    }

    public List<CommentTaskResponse> getAllMyCommentTasks() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CommentTask> allByAssignedUserId = commentTaskRepository.findAllByAuthorId(userId);

        return allByAssignedUserId.stream()
                .map(commentTaskMapper::toTaskResponse)
                .toList();
    }


    public CommentTaskResponse getCommentTaskById(String commentTaskId) {
        CommentTask commentTask = commentTaskRepository.findById(commentTaskId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return commentTaskMapper.toTaskResponse(commentTask);
    }

    public CommentTaskResponse createCommentTask(CommentTaskCreationRequest commentTaskCreationRequest) {
        CommentTask commentTask = commentTaskMapper.toCommentTask(commentTaskCreationRequest);

        commentTask.setTimestamp(Instant.now());

        return commentTaskMapper.toTaskResponse(commentTaskRepository.save(commentTask));
    }
    
    public CommentTaskResponse updateCommentTask(String taskId, CommentTaskUpdateRequest commentTaskUpdateRequest) {
        return null;
    }
    
    public String deleteCommentTask(String commentTaskId) {
        return null;
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
