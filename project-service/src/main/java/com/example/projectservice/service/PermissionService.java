package com.example.projectservice.service;

import com.example.projectservice.entity.Member;
import com.example.projectservice.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    MemberRepository memberRepository;

    public boolean hasRoleWithMemberId(String memberId, String roleName) {
        Member member = memberRepository.findById(memberId).orElse(null);

        return member != null && hasRole(member.getProject().getId(), roleName);
    }
    
    public boolean hasRole(String projectId, String roleName) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return hasRole(projectId, userId, roleName);
    }

    public boolean hasRole(String projectId, String userId, String roleName) {
        Member member = memberRepository.findByUserIdAndProject_Id(userId, projectId).orElse(null);

        if (member != null) {
            if ("*".equals(roleName)) {
                roleName = member.getRole().getName();
            }
            return member.getRole().getName().equals(roleName);
        }

        return false;
    }
}
