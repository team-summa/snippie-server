package com.snippie.backend.summary.service;

import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.domain.CodeDiffInput;
import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.SummaryType;
import com.snippie.backend.summary.domain.TextInput;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.repository.CodeDiffInputRepository;
import com.snippie.backend.summary.repository.SummaryRepository;
import com.snippie.backend.summary.repository.TextInputRepository;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;
    private final TextInputRepository textInputRepository;
    private final CodeDiffInputRepository codeDiffInputRepository;
    private final UserRepository userRepository;

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

        String summaryContent = generateSummary(request);
        Summary summary = new Summary();
        summary.setUser(user);
        summary.setSummaryType(SummaryType.valueOf(request.getType()));
        summary.setTitle(request.getTitle());
        summary.setContent(summaryContent);

        summaryRepository.save(summary);

        if(hasText) {
            TextInput input = new TextInput();
            input.setSummary(summary);
            input.setInputText(request.getInputText());
            textInputRepository.save(input);
            summary.setTextInput(input);
        } else if (hasCode) {
            CodeDiffInput codeDiffInput = new CodeDiffInput();
            codeDiffInput.setSummary(summary);
            codeDiffInput.setBeforeCode(request.getBeforeCode());
            codeDiffInput.setAfterCode(request.getAfterCode());
            codeDiffInputRepository.save(codeDiffInput);
            summary.setCodeDiffInput(codeDiffInput);
        }
        return new SummaryResponseDto(
                summary.getId(),
                summary.getSummaryType().name(),
                summary.getTitle(),
                summary.getContent()
        );
    }

    // 일단 Mock
    private String generateSummary(SummaryRequestDto request) {
        if (request.getInputText() != null) {
            return "자연어 기반 요약 생성 결과";
        } else {
            return "코드 변경 기반 요약 생성 결과입니다.";
        }
    }

}

