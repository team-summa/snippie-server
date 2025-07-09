package com.snippie.backend.user.controller;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.common.exception.ErrorResponse;
import com.snippie.backend.user.dto.UserResponseDto;
import com.snippie.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "user API", description = "사용자 정보 조회, 삭제 API")
public class UserController {
    private final UserService service;

    @Operation(
            summary = "회원 정보 조회",
            description = """
                    로그인된 회원 정보를 조회합니다.
                    
                    - 존재하지 않는 회원 조회 시 404 에러 발생
                    - type 파라미터에 따라 요약을 조회합니다.
                    - type은 반드시 COMMIT, ISSUE, PR 중 하나만 가능 (그 외는 400 에러)
                    - type = null인 경우 전체 요약을 조회합니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "회원 조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
    )
    @ApiResponse(responseCode = "404", description = "회원 조회 실패",
            content = @Content(
                    examples = @ExampleObject(
                            value = "{ " +
                                    "\"timestamp\": \"2025-07-07T01:42:29.4323376\", " +
                                    "\"status\": 404, " +
                                    "\"error\": \"NOT_FOUND\", " +
                                    "\"code\": null, " +
                                    "\"message\": \"해당 아이디로 유저를 찾을 수 없습니다.\", " +
                                    "\"path\": \"/api/users/me\"" +
                                    " }"
                    )
            )
    )
    @ApiResponse(responseCode = "400", description = "잘못된 type 요청",
            content = @Content(
                    examples = @ExampleObject(
                            value = "{ " +
                                    "\"timestamp\": \"2025-07-07T01:42:29.4323376\", " +
                                    "\"status\": 400, " +
                                    "\"error\": \"BAD_REQUEST\", " +
                                    "\"code\": null, " +
                                    "\"message\": \"유효하지 않은 요약 타입입니다.\", " +
                                    "\"path\": \"/api/users/me\"" +
                                    " }"
                    )
            )
    )
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @Parameter(name = "type", description = "요약 type", required = false, example = "ISSUE")
            String type
    ) {
        long userId = (user != null) ? user.getId() : 5L;

        return ResponseEntity.ok().body(service.getUserInfo(userId, type));
    }


}
