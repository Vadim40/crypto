package com.example.authenticationservice.Services;

import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import com.example.authenticationservice.Services.Interfaces.JwtTokenService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final OtpService otpService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String verifyOtpAndGenerateJwt( String otp) {
       String email=userDetailsService.getAuthenticatedUser().getEmail();
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

    @Override
    public void authenticate(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
           throw new BadCredentialsException("wrong password or login");
        }
    }
}
