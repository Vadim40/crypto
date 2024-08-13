package com.example.wallet.Models.DTO;

public record ErrorResponse(
        String error,
        String message,
        int status,
        String path
) {

}
