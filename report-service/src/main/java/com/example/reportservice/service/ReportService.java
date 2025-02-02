package com.example.reportservice.service;

import com.example.reportservice.contant.PredefinedMemberRole;
import com.example.reportservice.contant.PredefinedTaskStatus;
import com.example.reportservice.dto.response.*;
import com.example.reportservice.mapper.ProjectMapper;
import com.example.reportservice.repository.httpclient.ProjectClient;
import com.example.reportservice.repository.httpclient.TaskClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    ProjectClient projectClient;
    TaskClient taskClient;
    ProjectMapper projectMapper;

    public ReportProjectResponse getReportProject(String projectId) {
        ProjectResponse projectResponse = projectClient.getProjectById(projectId).getResult();
        List<MemberResponse> memberResponseList = projectClient.getMembers(projectId).getResult();
        List<TaskResponse> taskResponseList = taskClient.getTasksByProjectId(projectId).getResult();

        BasicInformationProjectResponse basicInformationProjectResponse =
                projectMapper.toBasicInformationProjectResponse(projectResponse);

        ManagementInformationProjectResponse managementInformationProjectResponse =
                ManagementInformationProjectResponse.builder()
                        .managerIds(memberResponseList.stream()
                                .filter(memberResponse ->
                                        memberResponse.getRole().getName().equals(PredefinedMemberRole.ROLE_MANAGER))
                                .map(MemberResponse::getUserId).toList())
                        .createdBy(projectResponse.getCreatedBy())
                        .build();

        QuantityInformationProjectResponse quantityInformationProjectResponse =
                _toQuantityInformationProjectResponse(managementInformationProjectResponse,
                        memberResponseList, taskResponseList);

        return ReportProjectResponse.builder()
                .basic(basicInformationProjectResponse)
                .management(managementInformationProjectResponse)
                .quantity(quantityInformationProjectResponse)
                .build();
    }

    private QuantityInformationProjectResponse _toQuantityInformationProjectResponse(
            ManagementInformationProjectResponse managementInformationProjectResponse,
            List<MemberResponse> memberResponseList,
            List<TaskResponse> taskResponseList) {
        int membersCount = memberResponseList.stream().filter(memberResponse ->
                memberResponse.getRole().getName().equals(PredefinedMemberRole.ROLE_MEMBER)).toList().size();
        int viewerCount = memberResponseList.stream().filter(memberResponse ->
                memberResponse.getRole().getName().equals(PredefinedMemberRole.ROLE_VIEWER)).toList().size();

        int todoCount = taskResponseList.stream().filter(taskResponse ->
                taskResponse.getStatus().getName().equals(PredefinedTaskStatus.STATUS_TODO)).toList().size();
        int inProgressCount = taskResponseList.stream().filter(taskResponse ->
                taskResponse.getStatus().getName().equals(PredefinedTaskStatus.STATUS_IN_PROCESSING)).toList().size();
        int completedTasks = taskResponseList.stream().filter(taskResponse ->
                taskResponse.getStatus().getName().equals(PredefinedTaskStatus.STATUS_COMPLETED)).toList().size();

        return QuantityInformationProjectResponse.builder()
                .managerCount(managementInformationProjectResponse.getManagerIds().size())
                .membersCount(membersCount)
                .viewerCount(viewerCount)
                .tasksCount(taskResponseList.size())
                .todoCount(todoCount)
                .inProgressCount(inProgressCount)
                .completedCount(completedTasks)
                .build();
    }
}
