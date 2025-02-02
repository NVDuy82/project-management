package com.example.projectservice.controller;

import com.example.projectservice.dto.request.MemberCreationRequest;
import com.example.projectservice.dto.request.MemberUpdateRequest;
import com.example.projectservice.dto.request.ProjectCreationRequest;
import com.example.projectservice.dto.request.ProjectUpdateRequest;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.MemberResponse;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.service.MemberService;
import com.example.projectservice.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/member")
public class MemberController {
    MemberService memberService;

    @GetMapping("/{memberId}")
    ApiResponse<MemberResponse> getMember(@PathVariable String memberId) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.getMemberById(memberId))
                .build();
    }

    @GetMapping("/get-members")
    ApiResponse<List<MemberResponse>> getMembersByProjectId(@RequestParam String projectId) {
        return ApiResponse.<List<MemberResponse>>builder()
                .result(memberService.getMembersByProjectId(projectId))
                .build();
    }

    @PostMapping("/add")
    ApiResponse<MemberResponse> createMember(@RequestBody @Valid MemberCreationRequest memberCreationRequest) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.createMember(memberCreationRequest))
                .build();
    }
    
    @PutMapping("/{memberId}")
    ApiResponse<MemberResponse> updateMember(@PathVariable String memberId,
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.updateMember(memberId, memberUpdateRequest))
                .build();
    }
    
    @DeleteMapping("/{memberId}")
    ApiResponse<String> removeMember(@PathVariable String memberId) {
        return ApiResponse.<String>builder()
                .result(memberService.deleteMember(memberId))
                .build();
    }

    @PostMapping("/leave-project")
    ApiResponse<String> leaveProject(@RequestParam String projectId) {
        return ApiResponse.<String>builder()
                .result(memberService.leaveProject(projectId))
                .build();
    }

    @PostMapping("/search")
    ApiResponse<List<MemberResponse>> search(@RequestParam String projectId, @RequestParam String query) {
        return ApiResponse.<List<MemberResponse>>builder()
                .result(memberService.search(projectId, query))
                .build();
    }
}
