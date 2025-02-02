package com.example.projectservice.service;

import com.example.projectservice.dto.request.ProjectStatusCreationRequest;
import com.example.projectservice.dto.request.ProjectStatusUpdateRequest;
import com.example.projectservice.dto.response.ProjectStatusResponse;
import com.example.projectservice.entity.Status;
import com.example.projectservice.exception.AppException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.mapper.StatusMapper;
import com.example.projectservice.repository.StatusRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatusService {
    StatusRepository statusRepository;
    StatusMapper statusMapper;
    
    public List<ProjectStatusResponse> getAllStatuses() {
        List<Status> all = statusRepository.findAll();
        
        return all.stream().map(statusMapper::toStatusResponse).toList();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectStatusResponse createStatus(ProjectStatusCreationRequest projectStatusCreationRequest) {
        Status status = statusMapper.toStatus(projectStatusCreationRequest);
        status = statusRepository.save(status);
        
        return statusMapper.toStatusResponse(status);
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectStatusResponse updateStatus(String roleId, ProjectStatusUpdateRequest projectStatusUpdateRequest) {
        Status status = statusRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        
        statusMapper.updateStatus(status, projectStatusUpdateRequest);
        status = statusRepository.save(status);
        
        return statusMapper.toStatusResponse(status);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStatus(String roleId) {
        statusRepository.deleteById(roleId);
        
        return "Status has been deleted";
    }
}
