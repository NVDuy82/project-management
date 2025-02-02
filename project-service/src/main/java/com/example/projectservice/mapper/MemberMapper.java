package com.example.projectservice.mapper;

import com.example.projectservice.dto.request.MemberUpdateRequest;
import com.example.projectservice.dto.response.MemberResponse;
import com.example.projectservice.entity.Member;
import com.example.projectservice.repository.httpclient.ProfileClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    /**
     * Custom mapper
     */
    default MemberResponse toMemberResponse(Member member, RoleMapper roleMapper, ProfileClient profileClient) {
        return MemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .projectId(member.getProject().getId())
                .role(roleMapper.toRoleResponse(member.getRole()))
                .userProfile(profileClient.getProfile(member.getUserId()).getResult())
                .build();
    }
}
