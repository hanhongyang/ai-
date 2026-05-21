package com.example.aigateway.exception;

import lombok.Getter;

@Getter
public class AiGatewayException extends RuntimeException {

    private final String errorCode;

    public AiGatewayException(String message) {
        super(message);
        this.errorCode = "UNKNOWN";
    }

    public AiGatewayException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AiGatewayException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "UNKNOWN";
    }
}
