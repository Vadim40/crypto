package com.example.notification.Kafka.DTOs;

public record OtpVerification(
        Long accountId,
        String email,
        int otp
) {
}
