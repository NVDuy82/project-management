package com.example.historyservice.service;

import com.example.historyservice.dto.request.ProjectHistoryCreationRequest;
import com.example.historyservice.dto.request.ProjectHistoryUpdateRequest;
import com.example.historyservice.dto.response.ProjectHistoryResponse;
import com.example.historyservice.enity.ProjectHistory;
import com.example.historyservice.mapper.ProjectHistoryMapper;
import com.example.historyservice.repository.ProjectChangeTypeRepository;
import com.example.historyservice.repository.ProjectHistoryRepository;
import com.example.historyservice.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectHistoryService {
    ProjectHistoryRepository projectHistoryRepository;
    ProjectChangeTypeRepository projectChangeTypeRepository;
    ProjectHistoryMapper projectHistoryMapper;
    ProfileClient profileClient;


    public List<ProjectHistoryResponse> getAllProjectHistorys() {
        return projectHistoryRepository.findAll()
                .stream().map(projectHistoryMapper::toProjectHistoryResponse).toList();
    }

    public ProjectHistoryResponse getProjectHistoryById(String id) {
        return projectHistoryMapper.toProjectHistoryResponse(projectHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not exist")));
    }

    public ProjectHistoryResponse createProjectHistory(
            ProjectHistoryCreationRequest ProjectHistoryCreationRequest) {
        ProjectHistory projectHistory =
                projectHistoryMapper.toProjectHistory(ProjectHistoryCreationRequest);
        projectHistory.setTimestamp(Instant.now());
        projectHistory = projectHistoryRepository.save(projectHistory);

        return projectHistoryMapper.toProjectHistoryResponse(projectHistory);
    }

    public ProjectHistoryResponse updateProjectHistory(String id,
                                                             ProjectHistoryUpdateRequest ProjectHistoryUpdateRequest) {
        ProjectHistory projectHistory = projectHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not exist"));

        projectHistoryMapper.updateProjectHistory(projectHistory, ProjectHistoryUpdateRequest);
        projectHistory = projectHistoryRepository.save(projectHistory);

        return projectHistoryMapper.toProjectHistoryResponse(projectHistory);
    }

    public String deleteProjectHistory(String id) {
        projectHistoryRepository.deleteById(id);

        return "Type has been deleted";
    }
}
