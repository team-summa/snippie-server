package com.snippie.backend.auth.controller;

import com.snippie.backend.auth.dto.UserPrincipal;
import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Operation(
            summary = "깃허브 소셜 로그인 API",
            description = "깃허브 로그인")
    @GetMapping("/github/login")
    public void redirectToGithub(HttpServletResponse response) throws IOException {
        // OAuth2 로그인 기본 경로로 리다이렉트
        response.sendRedirect("/oauth2/authorization/github");
    }

    @Operation(
            summary = "로그인 상태 확인 API",
            description = "현재 사용자가 로그인 상태인지 확인합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 여부 응답",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                                    {
                                      "loggedIn": true,
                                      "userId": 1
                                    }
                                    """
                    )
            )
    )
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkLogin(@AuthenticationPrincipal UserPrincipal user) {
        if (user == null) {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        return ResponseEntity.ok(Map.of(
                "loggedIn", true,
                "userId", user.getId()
        ));
    }
}
