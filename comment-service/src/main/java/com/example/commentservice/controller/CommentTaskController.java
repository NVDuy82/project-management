package com.example.commentservice.controller;

import com.example.commentservice.dto.request.CommentTaskCreationRequest;
import com.example.commentservice.dto.request.CommentTaskUpdateRequest;
import com.example.commentservice.dto.response.ApiResponse;
import com.example.commentservice.dto.response.CommentTaskResponse;
import com.example.commentservice.service.CommentTaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentTaskController {
    CommentTaskService commentTaskService;
    
    @GetMapping("/all")
    ApiResponse<List<CommentTaskResponse>> getAllCommentTasks() {
        return ApiResponse.<List<CommentTaskResponse>>builder()
                .result(commentTaskService.getAllCommentTasks())
                .build();
    }
    
    @GetMapping("/my-comment-tasks")
    ApiResponse<List<CommentTaskResponse>> getAllMyCommentTasks() {
        return ApiResponse.<List<CommentTaskResponse>>builder()
                .result(commentTaskService.getAllMyCommentTasks())
                .build();
    }

    @GetMapping("/{commentTaskId}")
    ApiResponse<CommentTaskResponse> getCommentTaskById(@PathVariable String commentTaskId) {
        return ApiResponse.<CommentTaskResponse>builder()
                .result(commentTaskService.getCommentTaskById(commentTaskId))
                .build();
    }


    @GetMapping("/all-comments-in-task")
    ApiResponse<List<CommentTaskResponse>> getAllTasks(@RequestParam String taskId) {
        return ApiResponse.<List<CommentTaskResponse>>builder()
                .result(commentTaskService.getAllCommentTasksByTaskId(taskId))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<CommentTaskResponse> createTask(
            @RequestBody CommentTaskCreationRequest commentTaskCreationRequest) {
        return ApiResponse.<CommentTaskResponse>builder()
                .result(commentTaskService.createCommentTask(commentTaskCreationRequest))
                .build();
    }

    @PutMapping("/{commentTaskId}")
    ApiResponse<CommentTaskResponse> updateTask(@PathVariable String commentTaskId,
                                                @RequestBody CommentTaskUpdateRequest commentTaskUpdateRequest) {
        return ApiResponse.<CommentTaskResponse>builder()
                .result(commentTaskService.updateCommentTask(commentTaskId, commentTaskUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/{commentTaskId}")
    ApiResponse<String> deleteProject(@PathVariable String commentTaskId) {
        return ApiResponse.<String>builder()
                .result(commentTaskService.deleteCommentTask(commentTaskId))
                .build();
    }
}
