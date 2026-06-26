package com.cts.jfs.sprinboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {

    private String operation;
    private String reason;

    // Constructor with operation and reason
    public InvalidOperationException(String operation, String reason) {
        super(String.format("Invalid operation '%s': %s", operation, reason));
        this.operation = operation;
        this.reason = reason;
    }

    // Simple message constructor
    public InvalidOperationException(String message) {
        super(message);
    }

    // Getters
    public String getOperation() { return operation; }
    public String getReason() { return reason; }
}
