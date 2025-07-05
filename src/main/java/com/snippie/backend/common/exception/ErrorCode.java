package com.snippie.backend.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 BAD REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    EMPTY_INPUT(HttpStatus.BAD_REQUEST, "C002", "입력값이 비어 있습니다."),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "C003", "필수 항목이 누락되었습니다."),
    INVALID_SUMMARY_TYPE(HttpStatus.BAD_REQUEST, "C005", "유효하지 않은 요약 타입입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    INVALID_GITHUB_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 Github 인증입니다."),

    // 404 NOT FOUND
    SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "요약을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "N002", "사용자를 찾을 수 없습니다."),

    // 409 CONFLICT
    DUPLICATE_SUMMARY(HttpStatus.CONFLICT, "C004", "이미 생성된 요약입니다."),

    // 429 TOO MANY REQUESTS
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "R001", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

    // 500 ERROR
    GPT_API_ERROR(HttpStatus.BAD_GATEWAY, "S001", "요약 생성 중 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S999", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String msg;

    ErrorCode(HttpStatus httpStatus, String code, String msg) {
        this.status = httpStatus;
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
