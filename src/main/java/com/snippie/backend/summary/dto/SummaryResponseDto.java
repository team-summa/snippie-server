package com.snippie.backend.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SummaryResponseDto {
    private Long summaryId;
    private String type;
    private String title;
    private String content;
}
