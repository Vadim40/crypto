package com.example.authenticationservice.Services;

import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import com.example.authenticationservice.Services.Interfaces.JwtTokenService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final OtpService otpService;
    private final CustomUserDetailsService userDetailsService;
    @Override
    public String verifyOtpAndGenerateJwt(String jwtToken, String otp) {
        String email = jwtTokenService.getUsername(jwtToken);
        otpService.approveOtp(otp, email);
       return jwtTokenService.generateToken(email);
    }

    @Override
    public String generateJwtBasedOnOtp(String email) {
        if (userDetailsService.isOtpEnabled(email)) {
            otpService.generateOtp(email);
            return jwtTokenService.generateTokenOtp(email);
        } else {
            return jwtTokenService.generateToken(email);
        }
    }
}
