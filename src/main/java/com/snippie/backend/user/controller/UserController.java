package com.snippie.backend.user.controller;

import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.dto.UserResponseDto;
import com.snippie.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("")
    public ResponseEntity<UserResponseDto> getUser(Long id, String type) {
        return ResponseEntity.ok().body(service.getUserInfo(id, type));
    }


}
