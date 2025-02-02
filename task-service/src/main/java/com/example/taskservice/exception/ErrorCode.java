package com.example.taskservice.exception;


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


    TASK_NOT_EXISTED(1301, "Task not existed", HttpStatus.NOT_FOUND),
    MUST_BE_MEMBER(1302, "User must be member", HttpStatus.BAD_REQUEST),
    TASK_STATUS_NOT_EXISTED(1303, "Status not existed", HttpStatus.NOT_FOUND),
    BAD_ASSIGNED(1304, "Assigned user remains unchanged", HttpStatus.BAD_REQUEST),
    INVALID_TASK_NAME(1305, "Name must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_TASK_DESCRIPTION(1306, "Description must be at 0-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_TASK_STATUS_NAME(1307, "Name must be at {min}-{max} characters", HttpStatus.BAD_REQUEST),
    INVALID_TASK_STATUS_DESCRIPTION(1308, "Description must be at 0-{max} characters", HttpStatus.BAD_REQUEST),
    CANT_DELETE_TASK(1309, "Can't delete task after 1 hour", HttpStatus.BAD_REQUEST),
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
