package com.example.gateway.DTO;

public record ErrorResponse(
        String error,
        String message,
        int status,
        String path
) {
}
