package com.example.identityservice.service;

import com.example.identityservice.dto.request.*;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.UserProfileResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.enity.Role;
import com.example.identityservice.enity.Status;
import com.example.identityservice.enity.User;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.mapper.ProfileMapper;
import com.example.identityservice.mapper.UserMapper;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.StatusRepository;
import com.example.identityservice.repository.UserRepository;
import com.example.identityservice.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    StatusRepository statusRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    ProfileClient profileClient;
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    
    @PostAuthorize("returnObject.id == authentication.name")
    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED)));
    }
    
    public UserResponse getMyInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return userMapper.toUserResponse(user);
    }

    public boolean existById(String userId) {
        return userRepository.existsById(userId);
    }

    public String getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return user.getId();
    }

    public String getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return user.getId();
    }
    
    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        
        User user = userMapper.toUser(userCreationRequest);
        user.setPasswordHash(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setCreateAt(Instant.now());
        
        // Set status
        Status status = statusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_STATUS_NAME));
        user.setStatus(status);
        
        // Set role
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);
        user.setRoles(roles);
        
        // Save
        user = userRepository.save(user);
        
        // Create profile
        UserProfileCreationRequest userProfileCreationRequest =
                profileMapper.toProfileCreationRequest(userCreationRequest);
        userProfileCreationRequest.setUserId(user.getId());
        ApiResponse<UserProfileResponse> profile = profileClient.createProfile(userProfileCreationRequest);
        
        if (profile.getCode() != 1000) {
            throw new AppException(ErrorCode.fromCode(profile.getCode()));
        }
        
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        
        userMapper.updateUser(user, userUpdateRequest);
//        user.setPasswordHash(passwordEncoder.encode(userUpdateRequest.getPassword()));
        
        List<Role> allById = roleRepository.findAllById(userUpdateRequest.getRoles());
        user.setRoles(new HashSet<>(allById));
        
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder
                .matches(changePasswordRequest.getPassword(), user.getPasswordHash());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        
        return "User has been deleted.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeStatus(ChangeStatusRequest changeStatusRequest) {
        String userId = changeStatusRequest.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Status status = statusRepository.findById(changeStatusRequest.getStatus())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setStatus(status);

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
