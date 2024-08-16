package com.example.authenticationservice.Models.DTOs;

public record AccountCreationRequest(
        String password,
        String email
) {
}
