package com.snippie.backend.summary.service.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.gpt.GptClient;
import com.snippie.backend.summary.gpt.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptSummaryService {

    private final GptClient gptClient;
    private final PromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;

    public GptResult generateSummary(SummaryRequestDto requestDto) {
        try {
            String gptResponse = gptClient.callGpt(promptBuilder.buildPrompt(
                    requestDto.getType().name(),
                    requestDto.getInputText(),
                    requestDto.getBeforeCode(),
                    requestDto.getAfterCode()
            ));

            // ✅ 1차 파싱: OpenAI API 응답
            Map<String, Object> responseMap = objectMapper.readValue(gptResponse, Map.class);

            // ✅ choices[0].message.content 추출
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String innerJsonString = (String) message.get("content");

            // ✅ 2차 파싱: GPT가 반환한 JSON
            Map<String, String> innerMap = objectMapper.readValue(innerJsonString, Map.class);

            String title = innerMap.getOrDefault("title", "요약 생성 실패");
            String content = innerMap.getOrDefault("content", "요약 내용 생성 실패");

            // ✅ usage 파싱
            Map<String, Object> usageMap = (Map<String, Object>) responseMap.getOrDefault("usage", Map.of());
            int promptTokens = (Integer) usageMap.getOrDefault("prompt_tokens", 0);
            int completionTokens = (Integer) usageMap.getOrDefault("completion_tokens", 0);
            int totalTokens = (Integer) usageMap.getOrDefault("total_tokens", 0);

            return new GptResult(title, content, promptTokens, completionTokens, totalTokens);

        } catch (Exception e) {
            return GptResult.failed();
        }
    }

}
