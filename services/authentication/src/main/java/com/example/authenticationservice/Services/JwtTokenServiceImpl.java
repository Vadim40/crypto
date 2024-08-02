package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.OtpNotEnabledException;
import com.example.authenticationservice.Services.Interfaces.JwtTokenService;
import com.example.authenticationservice.Utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public String generateToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtTokenUtils.generateToken(userDetails);
    }

    @Override
    public void verifyOtp(String jwtToken) {
        if (!jwtTokenUtils.isOtpToken(jwtToken)) {
            throw new OtpNotEnabledException("Otp is not enabled");
        }
    }

    @Override
    public String getUsername(String jwtToken) {
        return jwtTokenUtils.getUsername(jwtToken);
    }

    @Override
    public String generateTokenOtp(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtTokenUtils.generateTokenOtp(userDetails);
    }
}
