package com.snippie.backend.summary.dto;

import com.snippie.backend.summary.domain.Summary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SummaryResponseDto {
    private Long summaryId;
    private String type;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    public static SummaryResponseDto of(Summary s) {
        return SummaryResponseDto.builder()
                .summaryId(s.getId())
                .type(s.getSummaryType().name())
                .title(s.getTitle())
                .content(s.getContent())
                .createdAt(s.getCreatedAt())
                .promptTokens(s.getPromptTokens())
                .completionTokens(s.getCompletionTokens())
                .totalTokens(s.getTotalTokens())
                .build();
    }
}
