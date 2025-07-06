package com.snippie.backend.summary.controller;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.common.ratelimit.RateLimitStatus;
import com.snippie.backend.common.ratelimit.RateLimiter;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.service.SummaryService;
import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {
    private final SummaryService summaryService;
    private final UserService userService;
    private final RateLimiter rateLimiter;

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
                    .header("X-RateLimit-Daily-Used",      String.valueOf(after.getDailyUsed()))
                    .header("X-RateLimit-Daily-Remaining", String.valueOf(after.getDailyRemaining()))
                    .header("X-RateLimit-Sliding-Used",    String.valueOf(after.getSlidingUsed()))
                    .header("X-RateLimit-Sliding-Remaining", String.valueOf(after.getSlidingRemaining()))
                    .body(body);

        } catch (Exception ex) {
            throw ex;
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<SummaryDto> getSummary(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(userService.getUserSummaryDetails(id));
    }
}
