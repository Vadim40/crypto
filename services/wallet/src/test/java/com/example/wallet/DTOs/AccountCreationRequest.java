package com.example.wallet.DTOs;

public record AccountCreationRequest(
        String password,
        String email
) {
}
