package com.snippie.backend.user.service;

import com.snippie.backend.auth.security.UserPrincipal;
import com.snippie.backend.summary.dto.SummaryRequestDto;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.dto.UserResponseDto;

public interface UserService {

    public UserResponseDto getUserInfo(Long id, String type);

    SummaryDto getUserSummaryDetails(Long id);

    public void deleteSummary(Long id, UserPrincipal user);

}
