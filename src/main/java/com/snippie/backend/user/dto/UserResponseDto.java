package com.snippie.backend.user.dto;

import com.snippie.backend.summary.domain.Summary;
import com.snippie.backend.user.domain.User;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponseDto {

    private Long id;

    private String githubId;

    private String nickname;

    private String avatarUrl;

    private List<SummaryDto> summaries;


    public static UserResponseDto of(User user, List<Summary> summaries) {
        List<SummaryDto> summaryDtos = summaries.stream()
                .map(SummaryDto::of)
                .toList();
        
        return UserResponseDto.builder()
                .id(user.getId())
                .githubId(user.getGithubId())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .summaries(summaryDtos)
                .build();
    }

}
