package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Models.DTOs.RefreshRequest;
import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletRequest httpServletRequest) {
        String remoteIp = httpServletRequest.getRemoteAddr();
        authenticationService.authenticate(authRequest.email(), authRequest.password());
        JwtResponse jwtResponse = new JwtResponse(authenticationService.generateJwtBasedOnOtp(authRequest.email(), remoteIp));
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestParam String otp, HttpServletRequest httpServletRequest) {
        String remoteIp = httpServletRequest.getRemoteAddr();
        JwtResponse jwtResponse = new JwtResponse(authenticationService.verifyOtpAndGenerateJwt(otp, remoteIp));
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshRequest request, HttpServletRequest httpServletRequest) {
        String remoteIp = httpServletRequest.getRemoteAddr();
        JwtResponse jwtResponse = new JwtResponse(authenticationService.refreshJwt(request.refreshToken(), remoteIp));
        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(){
        authenticationService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}