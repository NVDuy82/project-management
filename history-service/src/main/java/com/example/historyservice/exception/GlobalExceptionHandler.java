package com.example.historyservice.exception;

import com.example.historyservice.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";
    
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        
        log.error("An unhandled exception occurred", exception);
        
        return ResponseEntity.badRequest().body(apiResponse);
    }
    
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<?>> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        
        try {
            String errorCodeKey = exception.getFieldError().getDefaultMessage();
            errorCode = ErrorCode.valueOf(errorCodeKey);
            
            ConstraintViolation unwrap = exception.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            
            attributes = unwrap.getConstraintDescriptor().getAttributes();
            
            log.info(attributes.toString());
            
        } catch (IllegalArgumentException e) {
        
        }
        
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes) ?
                        mapAttribute(errorCode.getMessage(), attributes) :
                        errorCode.getMessage())
                .build();
        
        return ResponseEntity.badRequest().body(apiResponse);
    }
    
    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));
        
        message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        message = message.replace("{" + MAX_ATTRIBUTE + "}", maxValue);
        
        return message;
    }
}
