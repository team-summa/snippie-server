package com.snippie.backend.user.dto;

import com.snippie.backend.summary.domain.Summary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SummaryDto {

    private Long id;
    private String summaryType;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static SummaryDto of(Summary summary) {
        return SummaryDto.builder()
                .id(summary.getId())
                .summaryType(String.valueOf(summary.getSummaryType()))
                .title(summary.getTitle())
                .content(summary.getContent())
                .createdAt(summary.getCreatedAt())
                .build();
    }
}
