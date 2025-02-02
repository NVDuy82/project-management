package com.example.projectservice.mapper;

import com.example.projectservice.dto.request.ProjectCreationRequest;
import com.example.projectservice.dto.request.ProjectUpdateRequest;
import com.example.projectservice.dto.response.MemberResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Member;
import com.example.projectservice.entity.Project;
import com.example.projectservice.repository.httpclient.ProfileClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toProject(ProjectCreationRequest projectCreationRequest);
    
    void updateProject(@MappingTarget Project project, ProjectUpdateRequest projectUpdateRequest);

    @Mapping(target = "createdBy", ignore = true)
    ProjectResponse _toProjectResponse(Project project);

    default ProjectResponse toProjectResponse(Project project, ProfileClient profileClient,
                                              MemberMapper memberMapper, RoleMapper roleMapper) {
        ProjectResponse projectResponse = _toProjectResponse(project);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member =
                project.getMembers().stream().filter(item -> item.getUserId().equals(userId)).toList().getFirst();


//        List<MemberResponse> list = project.getMembers().stream()
//                .map(member -> memberMapper.toMemberResponse(member, roleMapper, profileClient)).toList();
//        projectResponse.setMembers(new HashSet<>(list));

        projectResponse.setCreatedBy(profileClient.getProfile(project.getCreatedBy()).getResult());
        projectResponse.setMyRole(roleMapper.toRoleResponse(member.getRole()));

        return projectResponse;
    }
}
