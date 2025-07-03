package com.snippie.backend.summary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snippie.backend.summary.domain.SummaryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryRequestDto {

    @NotNull(message = "요약 유형(type)은 필수입니다.")
    private SummaryType type;
    private String inputText;
    private String beforeCode;
    private String afterCode;
}
