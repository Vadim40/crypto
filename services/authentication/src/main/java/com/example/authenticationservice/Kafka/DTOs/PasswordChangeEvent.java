package com.example.authenticationservice.Kafka.DTOs;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PasswordChangeEvent(
        Long accountId,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime changeTimestamp,
        String remoteIp
) {
}