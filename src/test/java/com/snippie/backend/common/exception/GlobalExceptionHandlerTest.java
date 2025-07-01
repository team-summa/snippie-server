package com.snippie.backend.common.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler 테스트")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("SnippieException이 발생하면 정의된 ErrorCode 기반 응답을 반환한다")
    void handleSnippieException() {
        SnippieException ex = new SnippieException(ErrorCode.SUMMARY_NOT_FOUND);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/summaries/123");

        ResponseEntity<ErrorResponse> response = handler.handleSnippieException(ex, request);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("요약을 찾을 수 없습니다.", response.getBody().getMessage());
        assertEquals("/api/summaries/123", response.getBody().getPath());
    }

    @Test
    @DisplayName("IllegalArgumentException은 400 에러로 처리된다")
    void handleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("파라미터 오류");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("파라미터 오류", response.getBody().getMessage());
    }

    @Test
    @DisplayName("알 수 없는 예외는 500 에러로 처리된다")
    void handleException() {
        Exception ex = new RuntimeException("예상치 못한 오류");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/error");

        ResponseEntity<ErrorResponse> response = handler.handleException(ex, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("서버 오류가 발생했습니다.", response.getBody().getMessage());
    }
}