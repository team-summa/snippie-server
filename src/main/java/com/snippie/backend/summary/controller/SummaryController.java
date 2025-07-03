package com.snippie.backend.summary.controller;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {
    private final SummaryService summaryService;

    @PostMapping
    public ResponseEntity<SummaryResponseDto> createSummary(
            @RequestBody @Valid SummaryRequestDto request,
            @AuthenticationPrincipal UserPrincipal user
            ) {
        Long userId = user.getId(); // 추후 다시 이 코드로 사용
//        Long userId = 1L; // 테스트용 하드코딩
        SummaryResponseDto response = summaryService.createSummary(request, userId);
        return ResponseEntity.ok(response);
    }
}
