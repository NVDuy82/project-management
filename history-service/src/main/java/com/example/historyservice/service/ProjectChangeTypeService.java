package com.example.historyservice.service;

import com.example.historyservice.dto.request.ProjectChangeTypeCreationRequest;
import com.example.historyservice.dto.request.ProjectChangeTypeUpdateRequest;
import com.example.historyservice.dto.response.ProjectChangeTypeResponse;
import com.example.historyservice.enity.ProjectChangeType;
import com.example.historyservice.mapper.ProjectChangeTypeMapper;
import com.example.historyservice.repository.ProjectChangeTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectChangeTypeService {
    ProjectChangeTypeRepository projectChangeTypeRepository;
    ProjectChangeTypeMapper projectChangeTypeMapper;

    public List<ProjectChangeTypeResponse> getAllProjectChangeTypes() {
        return projectChangeTypeRepository.findAll()
                .stream().map(projectChangeTypeMapper::toProjectChangeTypeResponse).toList();
    }

    public ProjectChangeTypeResponse getProjectChangeTypeById(String id) {
        return projectChangeTypeMapper.toProjectChangeTypeResponse(projectChangeTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type not exist")));
    }

    public ProjectChangeTypeResponse createProjectChangeType(
            ProjectChangeTypeCreationRequest projectChangeTypeCreationRequest) {
        ProjectChangeType projectChangeType =
                projectChangeTypeMapper.toProjectChangeType(projectChangeTypeCreationRequest);
        projectChangeType = projectChangeTypeRepository.save(projectChangeType);

        return projectChangeTypeMapper.toProjectChangeTypeResponse(projectChangeType);
    }

    public ProjectChangeTypeResponse updateProjectChangeType(String id,
            ProjectChangeTypeUpdateRequest projectChangeTypeUpdateRequest) {
        ProjectChangeType projectChangeType = projectChangeTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type not exist"));

        projectChangeTypeMapper.updateProjectChangeType(projectChangeType, projectChangeTypeUpdateRequest);
        projectChangeType = projectChangeTypeRepository.save(projectChangeType);

        return projectChangeTypeMapper.toProjectChangeTypeResponse(projectChangeType);
    }

    public String deleteProjectChangeType(String id) {
        projectChangeTypeRepository.deleteById(id);

        return "Type has been deleted";
    }
}
