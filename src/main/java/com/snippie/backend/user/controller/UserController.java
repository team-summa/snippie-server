package com.snippie.backend.user.controller;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.user.dto.UserResponseDto;
import com.snippie.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(
            @AuthenticationPrincipal UserPrincipal user,
            String type
    ) {
        Long id = user.getId();
        return ResponseEntity.ok().body(service.getUserInfo(id, type));
    }


}
