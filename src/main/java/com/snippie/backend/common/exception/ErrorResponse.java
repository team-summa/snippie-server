package com.snippie.backend.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {

    @Schema(description = "에러 발생 시각", example = "2025-07-06T13:00:11.744Z")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int status;

    @Schema(description = "HTTP 상태 설명", example = "BAD_REQUEST")
    private String error;

    @Schema(description = "서비스 에러 코드", example = "C001")
    private String code;

    @Schema(description = "에러 메시지", example = "요약할 텍스트가 너무 깁니다.")
    private String message;

    @Schema(description = "요청 경로", example = "/api/summaries")
    private String path;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String code, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getStatus().name(),
                errorCode.getCode(),
                customMessage,
                path
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getStatus().name(),
                errorCode.getCode(),
                errorCode.getMsg(),
                path
        );
    }

    public static ErrorResponse from(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                null,
                message,
                path
        );
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
