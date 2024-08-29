package com.example.authenticationservice.Services.Interfaces;

public interface AuthenticationService {
    String verifyOtpAndGenerateJwt( String otp, String remoteIp);
    String generateJwtBasedOnOtp(String email, String remoteIp);

    void authenticate(String username, String password);
}
