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

    @Override
    @Transactional
    public SummaryResponseDto createSummary(SummaryRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SnippieException(ErrorCode.USER_NOT_FOUND));

        validateRequest(request);

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
}