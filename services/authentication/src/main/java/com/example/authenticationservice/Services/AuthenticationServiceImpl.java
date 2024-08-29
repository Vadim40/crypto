package com.example.authenticationservice.Services;

import com.example.authenticationservice.Kafka.AuthenticationProducer;
import com.example.authenticationservice.Kafka.DTOs.UserLoginEvent;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import com.example.authenticationservice.Services.Interfaces.JwtTokenService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final OtpService otpService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationProducer authenticationProducer;
    private final AccountService accountService;

    @Override
    public String verifyOtpAndGenerateJwt( String otp, String remoteIp) {
        String email =userDetailsService.getAuthenticatedUser().getEmail();
        otpService.approveOtp(otp,email);
        sendUserLoginEvent(email, remoteIp);
        return jwtTokenService.generateToken(email);
    }

    private void sendUserLoginEvent(String email, String remoteIp) {
        Account account=accountService.findAccountByEmail(email);
        UserLoginEvent userLoginEvent=new UserLoginEvent(account.getId(), account.getEmail(), LocalDateTime.now(), remoteIp);        authenticationProducer.sendUserLoginEvent(userLoginEvent);
    }

    @Override
    public String generateJwtBasedOnOtp(String email, String remoteIp) {
        if (userDetailsService.isOtpEnabled(email)) {
            otpService.generateOtp(email);
            return jwtTokenService.generateTokenOtp(email);
        } else {
            sendUserLoginEvent(email,remoteIp);
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
