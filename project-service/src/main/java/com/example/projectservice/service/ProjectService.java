package com.example.projectservice.service;

import com.example.projectservice.contant.PredefinedRole;
import com.example.projectservice.contant.PredefinedStatus;
import com.example.projectservice.dto.request.*;
import com.example.projectservice.dto.response.PageResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Member;
import com.example.projectservice.entity.Project;
import com.example.projectservice.exception.AppException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.mapper.MemberMapper;
import com.example.projectservice.mapper.ProjectMapper;
import com.example.projectservice.mapper.RoleMapper;
import com.example.projectservice.repository.MemberRepository;
import com.example.projectservice.repository.ProjectRepository;
import com.example.projectservice.repository.RoleRepository;
import com.example.projectservice.repository.StatusRepository;
import com.example.projectservice.repository.httpclient.NotificationClient;
import com.example.projectservice.repository.httpclient.ProfileClient;
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
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectService {
    ProjectRepository projectRepository;
    MemberRepository memberRepository;
    RoleRepository roleRepository;
    StatusRepository statusRepository;
    ProjectMapper projectMapper;
    MemberMapper memberMapper;
    RoleMapper roleMapper;
    ProfileClient profileClient;
    NotificationClient notificationClient;

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProjectResponse> getAllProjects() {
        List<Project> all = projectRepository.findAll();
        
        return all.stream()
                .map(project -> projectMapper.
                        toProjectResponse(project, profileClient, memberMapper, roleMapper))
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public PageResponse<ProjectResponse> getMyProjects(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Project> pageData = projectRepository.findByUserId(userId, pageable);

        return PageResponse.<ProjectResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(project -> projectMapper
                        .toProjectResponse(project, profileClient, memberMapper, roleMapper)).toList())
                .build();
        
//        return myProjects.stream()
//                .map(project -> projectMapper.
//                        toProjectResponse(project, profileClient, memberMapper, roleMapper))
//                .toList();
    }

    @PreAuthorize("@permissionService.hasRole(#id, '*')")
    public ProjectResponse getById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        return projectMapper.toProjectResponse(project, profileClient, memberMapper, roleMapper);
    }
    
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse createProject(ProjectCreationRequest projectCreationRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Create project
        Project project = projectMapper.toProject(projectCreationRequest);
        project.setCreatedAt(Instant.now());
        project.setUpdatedAt(Instant.now());
        project.setCreatedBy(userId);
        project.setStatus(statusRepository.findById(PredefinedStatus.STATUS_ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_STATUS_NOT_EXISTED)));
        
        // Create member
        Member member = Member.builder()
                .userId(userId)
                .project(project)
                .role(roleRepository.findById(PredefinedRole.ROLE_MANAGER)
                        .orElseThrow(() -> new AppException(ErrorCode.MEMBER_ROLE_NOT_EXISTED)))
                .build();

        // Add member
        HashSet<Member> members = new HashSet<>();
        members.add(member);
        project.setMembers(members);

        project = projectRepository.save(project);
        memberRepository.save(member);
        return projectMapper.toProjectResponse(project, profileClient, memberMapper, roleMapper);
    }

    @PreAuthorize("@permissionService.hasRole(#projectId, " +
            "T(com.example.projectservice.contant.PredefinedRole).ROLE_MANAGER)")
    public ProjectResponse updateProject(String projectId, ProjectUpdateRequest projectUpdateRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        projectMapper.updateProject(project, projectUpdateRequest);
        project.setUpdatedAt(Instant.now());
        project = projectRepository.save(project);

        return projectMapper.toProjectResponse(project, profileClient, memberMapper, roleMapper);
    }

    public String deleteProject(String projectId) {
        return null;
    }

}
