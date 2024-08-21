package com.example.wallet.DTOs;

public record JwtRequest(
        String password,
        String email
) {
}
