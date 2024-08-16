package com.example.authenticationservice.Models.DTOs;

public record JwtRequest(
        String password,
        String email
) {
}
