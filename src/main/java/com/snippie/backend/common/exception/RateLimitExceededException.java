package com.snippie.backend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class RateLimitExceededException extends SnippieException {
    public RateLimitExceededException(String message) {
        super(ErrorCode.TOO_MANY_REQUESTS, message);
    }


}