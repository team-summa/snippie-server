package com.snippie.backend.common.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitStatus {
    private final long dailyUsed;
    private final long dailyRemaining;
    private final long slidingUsed;
    private final long slidingRemaining;
}
