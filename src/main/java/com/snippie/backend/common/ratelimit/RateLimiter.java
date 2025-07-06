package com.snippie.backend.common.ratelimit;

import com.snippie.backend.common.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Component
@RequiredArgsConstructor
public class RateLimiter {

    private final StringRedisTemplate redisTemplate;

    private static final int SLIDING_WINDOW_LIMIT = 20;
    private static final int DAILY_LIMIT          = 60;
    private static final long SLIDING_WINDOW_MS   = 3 * 60 * 60 * 1000;

    public RateLimitStatus checkLimit(long userId) {
        long now = System.currentTimeMillis();
        String sKey = "rate-limit:sliding:" + userId;
        String dKey = "rate-limit:daily:"  + userId;

        long slidingUsed = redisTemplate.opsForZSet()
                .count(sKey, now - SLIDING_WINDOW_MS, now);
        long dailyUsed   = Optional.ofNullable(redisTemplate.opsForValue().get(dKey))
                .map(Long::parseLong).orElse(0L);

        if (dailyUsed  >= DAILY_LIMIT)
            throw new RateLimitExceededException("24시간 이내 " + DAILY_LIMIT + "회까지 사용 가능합니다.");
        if (slidingUsed >= SLIDING_WINDOW_LIMIT)
            throw new RateLimitExceededException("3시간 당 " + SLIDING_WINDOW_LIMIT + "회까지 사용 가능합니다. 조금 기다렸다가 다시 시도해주세요.");

        return new RateLimitStatus(
                dailyUsed, DAILY_LIMIT - dailyUsed,
                slidingUsed, SLIDING_WINDOW_LIMIT - slidingUsed
        );
    }

    public RateLimitStatus commitUsage(long userId) {
        long now  = System.currentTimeMillis();
        String sKey = "rate-limit:sliding:" + userId;
        String dKey = "rate-limit:daily:"  + userId;

        redisTemplate.opsForZSet().add(sKey, String.valueOf(now), now);
        redisTemplate.opsForZSet().removeRangeByScore(sKey, 0, now - SLIDING_WINDOW_MS);
        redisTemplate.expire(sKey, Duration.ofHours(3));

        long dailyUsed = redisTemplate.opsForValue().increment(dKey);
        if (dailyUsed == 1) {
            long ttl = getSecondsUntilMidnight();
            redisTemplate.expire(dKey, ttl, TimeUnit.SECONDS);
        }

        long slidingUsed = redisTemplate.opsForZSet().size(sKey);
        return new RateLimitStatus(
                dailyUsed, DAILY_LIMIT - dailyUsed,
                slidingUsed, SLIDING_WINDOW_LIMIT - slidingUsed
        );
    }

    private long getSecondsUntilMidnight() {
        long now = System.currentTimeMillis();
        long midnight = now + (24 * 60 * 60 * 1000) - (now % (24 * 60 * 60 * 1000));
        return (midnight - now) / 1000;
    }
}
