package com.snippie.backend.user.service;

import com.snippie.backend.user.dto.SummaryDto;
import com.snippie.backend.user.dto.UserResponseDto;

public interface UserService {

    public UserResponseDto getUserInfo(Long id, String type);

    SummaryDto getUserSummaryDetails(Long id);

}
