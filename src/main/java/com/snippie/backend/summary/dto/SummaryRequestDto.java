package com.snippie.backend.summary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryRequestDto {

    @NotBlank(message = "요약 유형(type)은 필수입니다.")
    private String type;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String inputText;

    private String beforeCode;
    private String afterCode;
}
