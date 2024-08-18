package com.example.authenticationservice.Kafka.DTOs;

public record AccountCreationConfirmation(
        String email,
        Long id
) {
}
