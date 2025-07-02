package com.snippie.backend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // SnippieException
    @ExceptionHandler(SnippieException.class)
    public ResponseEntity<ErrorResponse> handleSnippieException(SnippieException ex, HttpServletRequest request) {
        ErrorCode ec = ex.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(ec, request.getRequestURI());
        return ResponseEntity.status(ec.getStatus()).body(errorResponse);
    }

    // 일반 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest requeset) {
        ErrorResponse errorResponse = ErrorResponse.from(HttpStatus.BAD_REQUEST, ex.getMessage(), requeset.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
