package com.example.authenticationservice.Kafka.DTOs;

public record OtpVerification(
        Long accountId,
        String email,
        int otp
) {
}
