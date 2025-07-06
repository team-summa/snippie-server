package com.snippie.backend.common.exception;

public class SnippieException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String logMessage;

    public SnippieException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.logMessage = null;
    }

    public SnippieException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.logMessage = customMessage;
    }

    public SnippieException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMsg(), cause);
        this.errorCode = errorCode;
        this.logMessage = null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getLogMessage() {
        return logMessage != null ? logMessage : errorCode.getMsg();
    }
}
