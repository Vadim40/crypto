package com.example.authenticationservice.Services.Interfaces;

public interface AuthenticationService {
    String verifyOtpAndGenerateJwt(String jwtToken, String otp);
    String generateJwtBasedOnOtp(String email);

    void authenticate(String username, String password);
}
