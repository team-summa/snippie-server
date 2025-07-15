package com.snippie.backend.user.service;

import com.snippie.backend.auth.dto.UserPrincipal;
import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.summary.domain.SummaryType;
import com.snippie.backend.summary.repository.SummaryRepository;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.dto.UserResponseDto;
import com.snippie.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final SummaryRepository summaryRepository;


    @Override
    public UserResponseDto getUserInfo(Long id, String type) {

        SummaryType summaryType = null;

        if(type != null){
            try {
                summaryType = SummaryType.valueOf(type.toUpperCase());
            }catch (IllegalArgumentException e){
                throw new SnippieException(ErrorCode.INVALID_SUMMARY_TYPE);
            }
        }

        List<Summary> summaries; //summaryList 받음
        if (summaryType == null) {
            summaries = summaryRepository.findAllByUserId(id);
        }else {
            summaries = summaryRepository.findAllByUserIdAndSummaryType(id, summaryType);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new SnippieException(ErrorCode.USER_NOT_FOUND, "해당 아이디로 유저를 찾을 수 없습니다."));
        return UserResponseDto.of(user, summaries);
    }

    @Override
    public SummaryDto getUserSummaryDetails(Long summaryId, Long userId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SnippieException(ErrorCode.SUMMARY_NOT_FOUND));

        if (!summary.getUser().getId().equals(userId)) {
            throw new SnippieException(ErrorCode.FORBIDDEN_SUMMARY_ACCESS, "해당 요약에 접근할 권한이 없습니다.");
        }

        return SummaryDto.of(summary);
    }

    @Override
    public void deleteSummary(Long id, UserPrincipal user) {
        Summary summary = summaryRepository.findById(id)
                .orElseThrow(() -> new SnippieException(ErrorCode.SUMMARY_NOT_FOUND));

        if (!summary.getUser().getId().equals(user.getId())) {
            throw new SnippieException(ErrorCode.FORBIDDEN_SUMMARY_ACCESS);
        }
        summaryRepository.delete(summary);
    }



}
