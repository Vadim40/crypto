package com.example.notification.Kafka.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
public record UserLoginEvent(
        Long accountId,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime loginTimestamp,
        String remoteIp
) {
}