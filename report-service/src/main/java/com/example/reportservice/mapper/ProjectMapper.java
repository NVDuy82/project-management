package com.example.reportservice.mapper;

import com.example.reportservice.dto.response.BasicInformationProjectResponse;
import com.example.reportservice.dto.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(target = "projectId", source = "id")
    BasicInformationProjectResponse toBasicInformationProjectResponse(ProjectResponse projectResponse);

    @Mapping(target = "projectId", source = "id")
    void updateBasicInformationProjectResponse(
            @MappingTarget BasicInformationProjectResponse basicInformationProjectResponse,
            ProjectResponse projectResponse);


}
