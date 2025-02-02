package com.example.historyservice.mapper;

import com.example.historyservice.dto.request.ProjectHistoryCreationRequest;
import com.example.historyservice.dto.request.ProjectHistoryUpdateRequest;
import com.example.historyservice.dto.response.ProjectHistoryResponse;
import com.example.historyservice.enity.ProjectHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectHistoryMapper {
    ProjectHistory toProjectHistory(ProjectHistoryCreationRequest projectHistoryCreationRequest);
    
    void updateProjectHistory(@MappingTarget ProjectHistory projectHistory,
                              ProjectHistoryUpdateRequest projectHistoryUpdateRequest);
    
    ProjectHistoryResponse toProjectHistoryResponse(ProjectHistory projectHistory);
}
