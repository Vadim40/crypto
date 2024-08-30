package com.example.authenticationservice.Services.Interfaces;

import java.util.Map;

public interface AuthenticationService {
    Map<String, String> verifyOtpAndGenerateJwt(String otp, String remoteIp);
    Map<String, String> generateJwtBasedOnOtp(String email, String remoteIp);
    Map<String, String> refreshJwt(String refreshToken, String remoteIp);

    void authenticate(String username, String password);
    void logout();
}
