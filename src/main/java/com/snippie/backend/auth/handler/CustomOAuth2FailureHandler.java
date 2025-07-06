package com.snippie.backend.auth.handler;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.ErrorResponse;
import com.snippie.backend.common.exception.SnippieException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 로그를 남기거나 필요하면 커스텀 메시지 생성
        SnippieException snippieException = new SnippieException(ErrorCode.INVALID_GITHUB_TOKEN, exception);

        ErrorResponse errorResponse = ErrorResponse.of(
                snippieException.getErrorCode(),
                snippieException.getLogMessage(),
                request.getRequestURI()
        );

        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
