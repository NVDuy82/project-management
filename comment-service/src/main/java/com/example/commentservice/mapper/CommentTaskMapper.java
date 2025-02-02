package com.example.commentservice.mapper;

import com.example.commentservice.dto.request.AssignRequest;
import com.example.commentservice.dto.request.CommentTaskCreationRequest;
import com.example.commentservice.dto.request.CommentTaskUpdateRequest;
import com.example.commentservice.dto.response.CommentTaskResponse;
import com.example.commentservice.enity.CommentTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentTaskMapper {

    @Mapping(target = "timestamp", ignore = true)
    CommentTask toCommentTask(CommentTaskCreationRequest commentTaskCreationRequest);

    @Mapping(target = "timestamp", ignore = true)
    void updateTask(@MappingTarget CommentTask commentTask,
                    CommentTaskUpdateRequest commentTaskUpdateRequest);
    
    CommentTaskResponse toTaskResponse(CommentTask commentTask);
}
