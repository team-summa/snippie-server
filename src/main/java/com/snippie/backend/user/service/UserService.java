package com.snippie.backend.user.service;

import com.snippie.backend.auth.dto.UserPrincipal;
import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto getUserInfo(Long id, String type);

    SummaryDto getUserSummaryDetails(Long summaryId, Long userId);

    void deleteSummary(Long id, UserPrincipal user);

}
