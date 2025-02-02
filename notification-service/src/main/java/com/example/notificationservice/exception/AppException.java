package com.example.notificationservice.exception;

import com.example.notificationservice.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
