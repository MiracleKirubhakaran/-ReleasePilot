package com.cts.jfs.sprinboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    // Simple message constructor
    public UnauthorizedException(String message) {
        super(message);
    }

    // Constructor with user info
    public UnauthorizedException(String email, String reason) {
        super(String.format("Unauthorized access for user '%s': %s", email, reason));
    }
}
