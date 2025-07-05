package com.snippie.backend.summary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.domain.CodeDiffInput;
import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.TextInput;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.gpt.GptClient;
import com.snippie.backend.summary.gpt.PromptBuilder;
import com.snippie.backend.summary.repository.SummaryRepository;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    private final GptClient gptClient;
    private final PromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public SummaryResponseDto createSummary(SummaryRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SnippieException(ErrorCode.USER_NOT_FOUND));

        boolean hasText = request.getInputText() != null && !request.getInputText().isBlank();
        boolean hasCode = request.getBeforeCode() != null && request.getAfterCode() != null;

        if (!hasText && !hasCode) {
            throw new SnippieException(ErrorCode.MISSING_REQUIRED_FIELD, "inputText 또는 beforeCode/afterCode 중 하나는 필수입니다.");
        }

        if (hasText && hasCode) {
            throw new SnippieException(ErrorCode.INVALID_INPUT_VALUE, "inputText와 beforeCode/afterCode는 동시에 보낼 수 없습니다.");
        }

        String[] gptResult = callGptWithFallback(request);

        Summary summary = Summary.builder()
                .user(user)
                .summaryType(request.getType())
                .title(gptResult[0])
                .content(gptResult[1])
                .build();

        if (hasText) {
            TextInput text = TextInput.builder()
                    .inputText(request.getInputText())
                    .build();
            summary.addTextInput(text);
        } else {
            CodeDiffInput diff = CodeDiffInput.builder()
                    .beforeCode(request.getBeforeCode())
                    .afterCode(request.getAfterCode())
                    .build();
            summary.addCodeDiffInput(diff);
        }

        summaryRepository.save(summary);
        return SummaryResponseDto.of(summary);
    }

    private String[] callGptWithFallback(SummaryRequestDto request) {
        try {
            String prompt = promptBuilder.buildPrompt(
                    request.getType().name(),
                    request.getInputText(),
                    request.getBeforeCode(),
                    request.getAfterCode()
            );

            String gptResponse = gptClient.callGpt(prompt);

            Map<String, String> resultMap = objectMapper.readValue(gptResponse, Map.class);

            String title = resultMap.getOrDefault("title", "요약 생성 실패");
            String content = resultMap.getOrDefault("content", "요약 내용 생성 실패");

            return new String[]{title, content};

        } catch (SnippieException | IOException e) {
            return new String[]{
                    "요약 생성 실패",
                    "요약 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
            };
        }
    }

}

