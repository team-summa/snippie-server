package com.snippie.backend.summary.dto;

import com.snippie.backend.summary.domain.Summary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SummaryResponseDto {
    private Long summaryId;
    private String type;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static SummaryResponseDto of(Summary s) {
        return new SummaryResponseDto(
                s.getId(),
                s.getSummaryType().name(),
                s.getTitle(),
                s.getContent(),
                s.getCreatedAt()
        );
    }
}
