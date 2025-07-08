package com.snippie.backend.summary.service;

import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;

public interface SummaryService {
    SummaryResponseDto createSummary(SummaryRequestDto request, Long userId);
}
