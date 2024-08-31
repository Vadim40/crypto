package com.example.notification.Kafka.DTOs;

import java.time.LocalDateTime;

public record UserLoginEvent(
        Long accountId,
        String email,
        LocalDateTime loginTimestamp,
        String remoteIp
) {
}