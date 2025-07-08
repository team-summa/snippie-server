package com.snippie.backend.summary.dto;

import com.snippie.backend.summary.domain.Summary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SummaryResponseDto {
    @Schema(description = "요약 ID(PK)", example = "42")
    private Long summaryId;

    @Schema(description = "요약 유형", example = "COMMIT")
    private String type;

    @Schema(description = "요약 제목", example = "refactor: 메소드 네이밍 개선")
    private String title;

    @Schema(description = "요약 내용", example = "- 기존 메소드 네이밍을 일관성 있게 수정하였습니다.")
    private String content;

    @Schema(description = "요약 생성일시", example = "2025-07-06T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "프롬프트 토큰 수", example = "123")
    private Integer promptTokens;

    @Schema(description = "완료 토큰 수", example = "456")
    private Integer completionTokens;

    @Schema(description = "총 토큰 수", example = "579")
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
