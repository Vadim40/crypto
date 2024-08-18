package com.example.wallet.Kafka.DTOs;

public record AccountCreationConfirmation(
        String email,
        Long id
) {
}