package com.example.authenticationservice.Services.Interfaces;

public interface JwtTokenService {
    String generateToken(String email);

    void verifyOtp(String jwtToken);

    String getUsername(String jwtToken);

    String generateTokenOtp(String email);
}
