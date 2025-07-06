package com.snippie.backend.summary.service;

import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.domain.CodeDiffInput;
import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.TextInput;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.repository.SummaryRepository;
import com.snippie.backend.summary.service.support.GptResult;
import com.snippie.backend.summary.service.support.GptSummaryService;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    private final GptSummaryService gptSummaryService;

    private static final int MAX_TEXT_LENGTH = 6000;
    private static final int MAX_CODE_LENGTH = 8000;

    @Override
    @Transactional
    public SummaryResponseDto createSummary(SummaryRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SnippieException(ErrorCode.USER_NOT_FOUND));

        validateRequest(request);
        validateInputLength(request);

        GptResult gptResult = gptSummaryService.generateSummary(request);

        Summary summary = Summary.builder()
                .user(user)
                .summaryType(request.getType())
                .title(gptResult.getTitle())
                .content(gptResult.getContent())
                .promptTokens(gptResult.getPromptTokens())
                .completionTokens(gptResult.getCompletionTokens())
                .totalTokens(gptResult.getTotalTokens())
                .build();

        if (request.getInputText() != null) {
            summary.addTextInput(TextInput.builder()
                    .inputText(request.getInputText())
                    .build());
        } else {
            summary.addCodeDiffInput(CodeDiffInput.builder()
                    .beforeCode(request.getBeforeCode())
                    .afterCode(request.getAfterCode())
                    .build());
        }

        summaryRepository.save(summary);
        return SummaryResponseDto.of(summary);
    }

    private void validateRequest(SummaryRequestDto request) {
        boolean hasText = request.getInputText() != null && !request.getInputText().isBlank();
        boolean hasCode = request.getBeforeCode() != null && request.getAfterCode() != null;

        if (!hasText && !hasCode) {
            throw new SnippieException(ErrorCode.MISSING_REQUIRED_FIELD, "inputText 또는 beforeCode/afterCode 중 하나는 필수입니다.");
        }

        if (hasText && hasCode) {
            throw new SnippieException(ErrorCode.INVALID_INPUT_VALUE, "inputText와 beforeCode/afterCode는 동시에 보낼 수 없습니다.");
        }
    }

    private void validateInputLength(SummaryRequestDto request) {
        if (request.getInputText() != null && request.getInputText().length() > MAX_TEXT_LENGTH) {
            throw new SnippieException(ErrorCode.INPUT_TOO_LONG, "요약할 텍스트가 너무 깁니다. (최대 1500자)");
        }
        if (request.getBeforeCode() != null && request.getBeforeCode().length() > MAX_CODE_LENGTH) {
            throw new SnippieException(ErrorCode.INPUT_TOO_LONG, "변경 전 코드가 너무 깁니다. (최대 5000자)");
        }
        if (request.getAfterCode() != null && request.getAfterCode().length() > MAX_CODE_LENGTH) {
            throw new SnippieException(ErrorCode.INPUT_TOO_LONG, "변경 후 코드가 너무 깁니다. (최대 5000자)");
        }
    }
}