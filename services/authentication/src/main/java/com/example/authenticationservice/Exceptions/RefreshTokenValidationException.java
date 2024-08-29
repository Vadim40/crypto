package com.example.authenticationservice.Exceptions;

public class RefreshTokenValidationException extends RuntimeException {
    public RefreshTokenValidationException(String message) {
        super(message);
    }
}
