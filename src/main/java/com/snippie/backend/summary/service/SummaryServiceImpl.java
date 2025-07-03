package com.snippie.backend.summary.service;

import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.domain.CodeDiffInput;
import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.TextInput;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.summary.dto.SummaryResponseDto;
import com.snippie.backend.summary.repository.SummaryRepository;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

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

        Summary summary = Summary.builder()
                .user(user)
                .summaryType(request.getType())
                .title(generateTitle(request))
                .content(generateSummary(request))
                .build();

        if (summary.getTextInput() != null || summary.getCodeDiffInput() != null) {
            throw new SnippieException(ErrorCode.DUPLICATE_SUMMARY, "이미 Summary에 Input이 존재합니다.");
        }

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
        entityManager.flush();
        entityManager.clear();
        return SummaryResponseDto.of(summary);
    }

    // 일단 Mock
    private String generateTitle(SummaryRequestDto request) {
        return (request.getInputText() != null)
                ? "자연어 기반 요약 타이틀"
                : "코드 변경 기반 요약 타이틀";
    }

    private String generateSummary(SummaryRequestDto request) {
        return (request.getInputText() != null)
                ? "자연어 기반 요약 결과"
                : "코드 변경 기반 요약 결과";
    }
}

