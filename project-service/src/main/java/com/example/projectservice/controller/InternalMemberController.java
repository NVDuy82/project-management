package com.example.projectservice.controller;

import com.example.projectservice.dto.request.MemberCreationRequest;
import com.example.projectservice.dto.request.MemberUpdateRequest;
import com.example.projectservice.dto.response.ApiResponse;
import com.example.projectservice.dto.response.MemberResponse;
import com.example.projectservice.service.MemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/member")
public class InternalMemberController {
    MemberService memberService;

    @GetMapping("/get-members")
    ApiResponse<List<MemberResponse>> getMembersByProjectId(@RequestParam String projectId) {
        return ApiResponse.<List<MemberResponse>>builder()
                .result(memberService.getMembersByProjectId(projectId))
                .build();
    }
}
