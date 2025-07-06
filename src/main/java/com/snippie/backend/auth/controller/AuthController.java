package com.snippie.backend.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Operation(
            summary = "깃허브 소셜 로그인 API",
            description = "깃허브 로그인")
    @GetMapping("/github/login")
    public void redirectToGithub(HttpServletResponse response) throws IOException {
        // OAuth2 로그인 기본 경로로 리다이렉트
        response.sendRedirect("/oauth2/authorization/github");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse response) {
        session.invalidate(); // 서버 세션 제거

        ResponseCookie deleteCookie = ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .maxAge(0) // 쿠키 제거
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString()); // 클라이언트 쿠키 삭제

        return ResponseEntity.ok("로그아웃 완료");
    }

}
