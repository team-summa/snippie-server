package com.snippie.backend.summary.service.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GptResult {
    private final String title;
    private final String content;
    private final int promptTokens;
    private final int completionTokens;
    private final int totalTokens;

    public static GptResult failed() {
        return new GptResult(
                "요약 생성 실패",
                "요약 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
                0, 0, 0
        );
    }
}
