package com.example.authenticationservice.Services;

import com.example.authenticationservice.Kafka.AuthenticationProducer;
import com.example.authenticationservice.Kafka.DTOs.UserLoginEvent;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Services.Interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final OtpService otpService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationProducer authenticationProducer;
    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Map<String, String> verifyOtpAndGenerateJwt(String otp, String remoteIp) {
        String email =userDetailsService.getAuthenticatedUser().getEmail();
        otpService.approveOtp(otp,email);
        sendUserLoginEvent(email, remoteIp);
        Map<String, String> tokens=new HashMap<>();
        String accessToken= jwtTokenService.generateToken(email);
        String refreshToken=refreshTokenService.createRefreshToken(email);
        tokens.put("accessToken",accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    private void sendUserLoginEvent(String email, String remoteIp) {
        Account account=accountService.findAccountByEmail(email);
        UserLoginEvent userLoginEvent=new UserLoginEvent(account.getId(), account.getEmail(), LocalDateTime.now(), remoteIp);        authenticationProducer.sendUserLoginEvent(userLoginEvent);
    }

    @Override
    public Map<String, String>generateJwtBasedOnOtp(String email, String remoteIp) {
        Map<String, String> tokens=new HashMap<>();
        if (userDetailsService.isOtpEnabled(email)) {
            otpService.generateOtp(email);
         String accessTokenOtp=jwtTokenService.generateTokenOtp(email);
         tokens.put("accessTokenOtp",accessTokenOtp);
        } else {

            String accessToken= jwtTokenService.generateToken(email);
            String refreshToken=refreshTokenService.createRefreshToken(email);
            tokens.put("accessToken",accessToken);
            tokens.put("refreshToken", refreshToken);
            sendUserLoginEvent(email,remoteIp);
        }
        return tokens;
    }

    @Override
    public Map<String,String> refreshJwt(String refreshToken,String remoteIp) {
        Map<String, String> tokens=new HashMap<>();
        String email=refreshTokenService.validateRefreshTokenReturnEmail(refreshToken);
        String accessToken=jwtTokenService.generateToken(email);
        tokens.put("accessToken",accessToken);
        sendUserLoginEvent(email,remoteIp);
        return tokens;
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

    @Override
    public void logout() {
        String email =userDetailsService.getAuthenticatedUser().getEmail();
        refreshTokenService.deleteTokenByAccountEmail(email);
    }
}
