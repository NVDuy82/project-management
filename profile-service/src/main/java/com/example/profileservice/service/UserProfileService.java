package com.example.profileservice.service;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.ProfileUpdateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.enity.UserProfile;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.exception.ErrorCode;
import com.example.profileservice.mapper.UserProfileMapper;
import com.example.profileservice.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    
    public UserProfileResponse createProfile(ProfileCreationRequest profileCreationRequest) {
        UserProfile userProfile = userProfileMapper.toUserProfile(profileCreationRequest);
        userProfile = userProfileRepository.save(userProfile);
        
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    
    public UserProfileResponse getByUserId(String userId) {
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();
        
        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }
    
    public UserProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfileByUserId(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userProfileMapper.updateProfile(userProfile, profileUpdateRequest);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    private boolean isValidEmail(String email) {
        // Định nghĩa regex kiểm tra email hợp lệ trực tiếp trong phương thức
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Biên dịch regex và kiểm tra email
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public List<UserProfileResponse> search(String query) {
        if (query == null) return null;

        query = query.trim();
        if (query.length() < 2) return new ArrayList<>();

        if (isValidEmail(query)) {
            return searchByEmail(query);
        } else {
            return searchByUsernameOrFullName(query);
        }
    }

    public List<UserProfileResponse> searchByUsernameOrFullName(String query) {
        List<UserProfile> profiles = userProfileRepository.searchByUsernameOrFullName(query);
        if (profiles.size() > 10) {
            profiles = profiles.subList(0, 10);
        }

        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public List<UserProfileResponse> searchByEmail(String query) {
        List<UserProfile> profiles = userProfileRepository.searchByEmail(query);
        if (profiles.size() > 10) {
            profiles = profiles.subList(0, 10);
        }

        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }
}
