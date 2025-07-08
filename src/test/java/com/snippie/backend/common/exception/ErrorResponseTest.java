package com.snippie.backend.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorResponse 테스트")
class ErrorResponseTest {

    @Test
    @DisplayName("ErrorCode를 통해 ErrorResponse가 올바르게 생성되는지 테스트")
    void of_creates_response_from_ErrorCode() {
        ErrorResponse res = ErrorResponse.of(ErrorCode.EMPTY_INPUT, "/path");

        assertEquals(400, res.getStatus());
        assertEquals("C002", res.getCode());
        assertEquals("입력값이 비어 있습니다.", res.getMessage());
        assertEquals("/path", res.getPath());
    }

    @Test
    @DisplayName("HttpStatus와 메시지를 통해 ErrorResponse가 생성된다")
    void from_creates_response_from_HttpStatus() {
        ErrorResponse res = ErrorResponse.from(HttpStatus.BAD_REQUEST, "입력 오류", "/test");

        assertEquals(400, res.getStatus());
        assertEquals("BAD_REQUEST", res.getError());
        assertNull(res.getCode());
        assertEquals("입력 오류", res.getMessage());
        assertEquals("/test", res.getPath());
    }
}