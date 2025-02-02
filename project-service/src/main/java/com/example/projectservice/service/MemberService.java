package com.example.projectservice.service;

import com.example.projectservice.contant.PredefinedRole;
import com.example.projectservice.dto.request.*;
import com.example.projectservice.dto.response.MemberResponse;
import com.example.projectservice.entity.Member;
import com.example.projectservice.exception.AppException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.mapper.MemberMapper;
import com.example.projectservice.mapper.ProjectMapper;
import com.example.projectservice.mapper.RoleMapper;
import com.example.projectservice.repository.MemberRepository;
import com.example.projectservice.repository.ProjectRepository;
import com.example.projectservice.repository.RoleRepository;
import com.example.projectservice.repository.httpclient.IdentityClient;
import com.example.projectservice.repository.httpclient.NotificationClient;
import com.example.projectservice.repository.httpclient.ProfileClient;
import com.example.projectservice.repository.httpclient.TaskClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MemberService {
    ProjectService projectService;
    ProjectRepository projectRepository;
    MemberRepository memberRepository;
    RoleRepository roleRepository;
    ProjectMapper projectMapper;
    MemberMapper memberMapper;
    RoleMapper roleMapper;
    IdentityClient identityClient;
    ProfileClient profileClient;
    TaskClient taskClient;
    NotificationClient notificationClient;

    public MemberResponse getMemberById(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        return memberMapper.toMemberResponse(member, roleMapper, profileClient);
    }

    public List<MemberResponse> getMembersByProjectId(String projectId) {
        List<Member> allByProjectId = memberRepository.findAllByProject_Id(projectId);

        return allByProjectId.stream()
                .map(member -> memberMapper.toMemberResponse(member, roleMapper, profileClient)).toList();
    }

    @PreAuthorize("@permissionService.hasRole(#memberCreationRequest.projectId, " +
            "T(com.example.projectservice.contant.PredefinedRole).ROLE_MANAGER)")
    public MemberResponse createMember(MemberCreationRequest memberCreationRequest) {

        if (memberRepository.existsByUserIdAndProject_Id(memberCreationRequest.getUserId(),
                memberCreationRequest.getProjectId())) {
            throw new AppException(ErrorCode.MEMBER_EXISTED);
        }

        if (!identityClient.existById(memberCreationRequest.getUserId()).getResult()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Can't add manager
        if (memberCreationRequest.getRole().equals(PredefinedRole.ROLE_MANAGER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_IN_PROJECT);
        }

        Member member = Member.builder()
                .userId(memberCreationRequest.getUserId())
                .project(projectRepository.findById(memberCreationRequest.getProjectId())
                        .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED)))
                .role(roleRepository.findById(memberCreationRequest.getRole())
                        .orElseThrow(() -> new AppException(ErrorCode.MEMBER_ROLE_NOT_EXISTED)))
                .build();

        member = memberRepository.save(member);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String name = profileClient.getProfile(userId).getResult().getFullName();
        notificationClient.create(NotificationCreationRequest.builder()
                .fromProjectId(member.getProject().getId())
                .fromTaskId(null)
                .toUserId(member.getUserId())
                .title("Được thêm vào project mới")
                .body("Bạn vừa được " + name + " thêm vào project " + member.getProject().getName())
                .build()
        );

        return memberMapper.toMemberResponse(member, roleMapper, profileClient);
    }

    @PreAuthorize("@permissionService.hasRoleWithMemberId(#id, " +
            "T(com.example.projectservice.contant.PredefinedRole).ROLE_MANAGER)")
    public MemberResponse updateMember(String id, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        if (!memberUpdateRequest.getRole().equals(PredefinedRole.ROLE_MANAGER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_IN_PROJECT);
        }

        if (!memberUpdateRequest.getRole().equals(member.getRole().getName())) {
            member.setRole(roleRepository.findById(memberUpdateRequest.getRole())
                    .orElseThrow(() -> new AppException(ErrorCode.MEMBER_ROLE_NOT_EXISTED)));
            member = memberRepository.save(member);
            notificationClient.create(NotificationCreationRequest.builder()
                    .fromProjectId(member.getProject().getId())
                    .fromTaskId(null)
                    .toUserId(member.getUserId())
                    .title("Thay đổi vai trò trong project")
                    .body("Vai trò của bạn được thay đổi thành " + member.getRole().getName()
                            + " trong project " + member.getProject().getName())
                    .build()
            );
        }

        return memberMapper.toMemberResponse(member, roleMapper, profileClient);
    }

    @PreAuthorize("@permissionService.hasRoleWithMemberId(#id, " +
            "T(com.example.projectservice.contant.PredefinedRole).ROLE_MANAGER)")
    public String deleteMember(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        if (member.getRole().getName().equals(PredefinedRole.ROLE_MANAGER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_IN_PROJECT);
        }

        memberRepository.delete(member);
        return "Member has been deleted";
    }

    @PreAuthorize("@permissionService.hasRole(#projectId, '*')")
    public String leaveProject(String projectId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByUserIdAndProject_Id(userId, projectId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        if (member.getRole().getName().equals(PredefinedRole.ROLE_MANAGER)) {
            List<Member> all = memberRepository.findAllByProject_Id(member.getProject().getId());
            List<Member> manager = all.stream().filter(m -> m.getRole().getName().equals(PredefinedRole.ROLE_MANAGER)).toList();

            if (manager.size() == 1 && all.size() > 1) {
                throw new AppException(ErrorCode.UNABLE_TO_LEAVE);
            }

            if (all.size() == 1) {
                memberRepository.delete(member);
                taskClient.deleteAllByProjectId(member.getProject().getId());
                projectRepository.deleteById(projectId);
                return "Member has been deleted";
            }
        }

        memberRepository.delete(member);
        return "Member has been deleted";
    }

    private boolean isValidEmail(String email) {
        // Định nghĩa regex kiểm tra email hợp lệ trực tiếp trong phương thức
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Biên dịch regex và kiểm tra email
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public List<MemberResponse> search(String projectId, String query) {
        String finalQuery = query.trim().toLowerCase();

        if (isValidEmail(finalQuery)) {
            return searchByEmail(projectId, finalQuery);
        } else {
            return searchByUsernameOrFullName(projectId, finalQuery);
        }
    }

    public List<MemberResponse> searchByUsernameOrFullName(String projectId, String query) {
        List<MemberResponse> membersByProjectId = getMembersByProjectId(projectId);

        List<MemberResponse> list = membersByProjectId.stream()
                .filter(m ->
                        m.getUserProfile().getUsername().toLowerCase().contains(query) ||
                                m.getUserProfile().getFullName().toLowerCase().contains(query))
                .toList();

        if (list.size() > 10) {
            list = list.subList(0, 10);
        }
        return list;
    }

    public List<MemberResponse> searchByEmail(String projectId, String query) {
        List<MemberResponse> membersByProjectId = getMembersByProjectId(projectId);

        List<MemberResponse> list = membersByProjectId.stream()
                .filter(m ->
                        m.getUserProfile().getEmail().toLowerCase().contains(query))
                .toList();

        if (list.size() > 10) {
            list = list.subList(0, 10);
        }
        return list;
    }
}
