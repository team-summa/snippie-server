package com.snippie.backend.common.exception;

public class RateLimitExceededException extends SnippieException {
    public RateLimitExceededException(String message) {
        super(ErrorCode.TOO_MANY_REQUESTS, message);
    }
}