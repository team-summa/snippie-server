package com.snippie.backend.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SnippieException 테스트")
class SnippieExceptionTest {

    @Test
    @DisplayName("기본 생성자: 메시지와 에러코드가 올바르게 설정된다")
    void constructor_sets_message_and_errorCode() {
        SnippieException ex = new SnippieException(ErrorCode.USER_NOT_FOUND);

        assertEquals("사용자를 찾을 수 없습니다.", ex.getMessage());
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("로그 메시지가 주어졌을 때 getLogMessage는 해당 메시지를 반환한다")
    void getLogMessage_returns_custom_message() {
        SnippieException ex = new SnippieException(ErrorCode.USER_NOT_FOUND, "DB 오류");

        assertEquals("DB 오류", ex.getLogMessage());
    }

    @Test
    @DisplayName("로그 메시지가 없으면 기본 메시지를 반환한다")
    void getLogMessage_returns_default_if_null() {
        SnippieException ex = new SnippieException(ErrorCode.USER_NOT_FOUND);

        assertEquals("사용자를 찾을 수 없습니다.", ex.getLogMessage());
    }
}