package com.example.authenticationservice.Services.Interfaces;

import com.example.authenticationservice.Models.CustomUserDetails;

public interface JwtTokenService {
    String generateToken(String email);

    void verifyOtp(String jwtToken);

    String getUsername(String jwtToken);

    String generateTokenOtp(String email);
}
