package com.snippie.backend.summary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snippie.backend.summary.domain.SummaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryRequestDto {

    @NotNull(message = "요약 유형(type)은 필수입니다.")
    @Schema(description = "요약 유형 (COMMIT, ISSUE, PR 중 하나)", example = "COMMIT", required = true)
    private SummaryType type;

    @Schema(description = "요약할 텍스트 (type이 TEXT(자연어)일 때 사용)", example = "이것은 테스트 입력입니다.")
    private String inputText;

    @Schema(description = "변경 전 코드 (type이 CODE(코드)일 때 사용)", example = "int a = 1;")
    private String beforeCode;

    @Schema(description = "변경 후 코드 (type이 CODE(코드)일 때 사용)", example = "int a = 2;")
    private String afterCode;
}
