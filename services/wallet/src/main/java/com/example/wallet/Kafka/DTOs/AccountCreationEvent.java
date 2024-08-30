package com.example.wallet.Kafka.DTOs;

public record AccountCreationEvent(
        String email,
        Long id
) {
}