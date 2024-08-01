package com.example.authenticationservice.Services.Interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    String verifyOtpAndGenerateJwt(String jwtToken, String otp);
    String generateJwtBasedOnOtp(String email);
}
