package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletRequest httpServletRequest) {
        String remoteIp=httpServletRequest.getRemoteAddr();
        authenticationService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        JwtResponse jwtResponse = new JwtResponse(authenticationService.generateJwtBasedOnOtp(authRequest.getEmail(),remoteIp));
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp( @RequestParam String otp, HttpServletRequest httpServletRequest) {
        String remoteIp=httpServletRequest.getRemoteAddr();
        JwtResponse jwtResponse = new JwtResponse(authenticationService.verifyOtpAndGenerateJwt(otp, remoteIp));
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
//    @PostMapping("/logout")
//    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
//        SecurityContextHolder.clearContext();
//
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//                response.addCookie(cookie);
//            }
//        }
//
//        response.setHeader("Authorization", "");
//
//
//        return new ResponseEntity<>( HttpStatus.OK);
//    }


}