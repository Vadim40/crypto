package com.example.notification.Kafka.DTOs;

public record AccountCreationEvent(
        String email,
        Long id
) {
}
