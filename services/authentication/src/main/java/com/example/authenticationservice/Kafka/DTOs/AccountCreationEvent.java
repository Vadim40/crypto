package com.example.authenticationservice.Kafka.DTOs;

public record AccountCreationEvent(
        String email,
        Long id
) {
}
