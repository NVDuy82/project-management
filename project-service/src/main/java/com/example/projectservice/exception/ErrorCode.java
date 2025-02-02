package com.example.projectservice.exception;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1002, "Username must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1004, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1004, "Username existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1004, "Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_SIGNATURE(1008, "Invalid signature", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1009, "Invalid token", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1010, "Permission not existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1011, "Role not existed", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1012, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_NAME(1013, "Invalid status name", HttpStatus.BAD_REQUEST),


    PROJECT_NOT_EXISTED(1201, "Project not existed", HttpStatus.NOT_FOUND),
    MEMBER_NOT_EXISTED(1202, "Member not existed", HttpStatus.NOT_FOUND),
    MEMBER_EXISTED(1203, "Member existed", HttpStatus.BAD_REQUEST),
    MEMBER_ROLE_NOT_EXISTED(1204, "Role not existed", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_IN_PROJECT(1205, "You do not have permission", HttpStatus.FORBIDDEN),
    UNABLE_TO_LEAVE(1206, "Unable to leave project", HttpStatus.BAD_REQUEST),
    PROJECT_STATUS_NOT_EXISTED(1208, "Status not existed", HttpStatus.NOT_FOUND),
    INVALID_PROJECT_NAME(1209, "Name must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_DESCRIPTION(1210, "Description must be at 0-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_ROLE_NAME(1211, "Name must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_ROLE_DESCRIPTION(1212, "Description must be at 0-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_STATUS_NAME(1213, "Name must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_STATUS_DESCRIPTION(1214, "Description must be at 0-{max} characters", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
    
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return UNCATEGORIZED_EXCEPTION;
    }
}
