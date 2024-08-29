package com.example.authenticationservice.Kafka.DTOs;


import java.time.LocalDateTime;

public record PasswordChangeEvent(
        Long accountId,
        String email,
        LocalDateTime changeTimestamp,
        String remoteIp
) {
}