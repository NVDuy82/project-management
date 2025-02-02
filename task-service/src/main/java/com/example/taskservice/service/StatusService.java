package com.example.taskservice.service;

import com.example.taskservice.dto.request.StatusCreationRequest;
import com.example.taskservice.dto.request.StatusUpdateRequest;
import com.example.taskservice.dto.response.TaskStatusResponse;
import com.example.taskservice.enity.Status;
import com.example.taskservice.exception.AppException;
import com.example.taskservice.exception.ErrorCode;
import com.example.taskservice.mapper.StatusMapper;
import com.example.taskservice.repository.StatusRepository;
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
    
    public List<TaskStatusResponse> getAllStatuses() {
        List<Status> all = statusRepository.findAll();
        
        return all.stream().map(statusMapper::toStatusResponse).toList();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public TaskStatusResponse createStatus(StatusCreationRequest statusCreationRequest) {
        Status status = statusMapper.toStatus(statusCreationRequest);
        status = statusRepository.save(status);
        
        return statusMapper.toStatusResponse(status);
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    public TaskStatusResponse updateStatus(String roleId, StatusUpdateRequest statusUpdateRequest) {
        Status status = statusRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        
        statusMapper.updateStatus(status, statusUpdateRequest);
        status = statusRepository.save(status);
        
        return statusMapper.toStatusResponse(status);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStatus(String roleId) {
        statusRepository.deleteById(roleId);
        
        return "Status has been deleted";
    }
}
