package com.snippie.backend.summary.controller;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.common.exception.ErrorResponse;
import com.snippie.backend.common.ratelimit.RateLimitStatus;
import com.snippie.backend.common.ratelimit.RateLimiter;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.service.SummaryService;
import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
@Tag(name = "Summary API", description = "요약 생성 및 조회 API")
public class SummaryController {
    private final SummaryService summaryService;
    private final UserService userService;
    private final RateLimiter rateLimiter;

    @Operation(
            summary = "요약 생성",
            description = """
                    요약 생성 API
                    
                    1. 요청 규칙
                    - type은 반드시 COMMIT, ISSUE, PR 중 하나만 가능
                    - inputText와 beforeCode/afterCode는 동시에 보낼 수 없음
                    
                    2. 텍스트 요약 요청
                    - inputText 필드만 채워서 요청
                    - 최대 6,000자까지 입력 가능
                    
                    3. 코드 변경 요약 요청
                    - beforeCode, afterCode 필드를 모두 채워서 요청
                    - 각각 최대 8,000자까지 입력 가능
                    
                    4. Rate Limit 정책
                    - 최근 3시간 동안 최대 20회 호출 가능
                    - 하루(24시간) 동안 최대 60회 호출 가능
                    - 호출 제한을 초과하면 HTTP 429 (Too Many Requests) 응답 반환
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요약 생성 성공",
                    headers = {
                            @Header(name = "X-RateLimit-Daily-Used", description = "하루 동안 사용한 요청 수"),
                            @Header(name = "X-RateLimit-Daily-Remaining", description = "하루 동안 남은 요청 가능 횟수"),
                            @Header(name = "X-RateLimit-Sliding-Used", description = "최근 3시간 동안 사용한 요청 수"),
                            @Header(name = "X-RateLimit-Sliding-Remaining", description = "최근 3시간 동안 남은 요청 가능 횟수")
                    },
                    content = @Content(schema = @Schema(implementation = SummaryResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "요청량 초과 -> 위 참고 (Rate Limit)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<SummaryResponseDto> createSummary(
            @RequestBody @Valid SummaryRequestDto request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        long userId = (user != null) ? user.getId() : 1L;

        RateLimitStatus before = rateLimiter.checkLimit(userId);

        try {
            SummaryResponseDto body = summaryService.createSummary(request, userId);

            RateLimitStatus after = rateLimiter.commitUsage(userId);

            return ResponseEntity.ok()
                    .header("X-RateLimit-Daily-Used", String.valueOf(after.getDailyUsed()))
                    .header("X-RateLimit-Daily-Remaining", String.valueOf(after.getDailyRemaining()))
                    .header("X-RateLimit-Sliding-Used", String.valueOf(after.getSlidingUsed()))
                    .header("X-RateLimit-Sliding-Remaining", String.valueOf(after.getSlidingRemaining()))
                    .body(body);

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Operation(
            summary = "요약 상세 조회",
            description = """
                    ID를 통해 특정 요약의 상세 정보를 조회합니다.
                    
                    - 사용자가 작성한 요약만 조회 가능
                    - 존재하지 않는 ID 조회 시 404 에러 반환
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 조회 성공",
                    content = @Content(schema = @Schema(implementation = SummaryDto.class))),
            @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SummaryDto> getSummary(
            @Parameter(description = "요약 ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(userService.getUserSummaryDetails(id));
    }

    @Operation(
            summary = "요약 삭제",
            description = """
                    요약 ID를 통해 통해 특정 요약을 삭제합니다.
                    
                    - 사용자가 작성한 요약만 삭제 가능 (403 에러 반환)
                    - 존재하지 않는 ID 조회 시 404 에러 반환
                    """
    )
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content)
    @ApiResponse(responseCode = "403", description = "삭제 권한이 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "요약 조회 실패", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<SummaryDto> deleteSummary(
            @Parameter(description = "요약 ID", example = "1")
            @PathVariable("id") Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user) {

        userService.deleteSummary(id, user);
        return ResponseEntity.ok().build();

    }
}
