package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Exceptions.OtpExpiredException;
import com.example.authenticationservice.Exceptions.OtpNotEnabledException;
import com.example.authenticationservice.Exceptions.OtpNotFoundException;
import com.example.authenticationservice.Models.CustomUserDetails;
import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Services.CustomUserDetailsService;
import com.example.authenticationservice.Services.OtpService;
import com.example.authenticationservice.Utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final OtpService otpService;


    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            Authentication authentication = authenticateUser(authRequest.getEmail(), authRequest.getPassword());
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authRequest.getEmail());

            String token;
            if (userDetails.isOTPEnabled()) {
                otpService.generateOtp(userDetails.getUsername());
                token = jwtTokenUtils.generateTokenOtp(userDetails);
            } else {
                token = jwtTokenUtils.generateToken(userDetails);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);

        } catch (BadCredentialsException e) {
            log.warn("Authentication failed: invalid credentials for user: {}", authRequest.getEmail());
            return new ResponseEntity<>("Wrong password or login", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error during authentication", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Authentication authenticateUser(String email, String password) throws BadCredentialsException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authentication);
        return authentication;
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String otp) {
        try {

            String jwtToken = authorizationHeader.substring(7);
            String email = jwtTokenUtils.getUsername(jwtToken);


            if (jwtTokenUtils.isOtpToken(jwtToken)) {
                if (otpService.verifyOtp(otp, email)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    String finalToken = jwtTokenUtils.generateToken(userDetails);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    return new ResponseEntity<>(new JwtResponse(finalToken), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Invalid OTP", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }

        } catch (OtpNotEnabledException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (OtpNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OtpExpiredException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping("/logout")
//    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false);
//        SecurityContextHolder.clearContext();
//        if (session != null) {
//            session.invalidate();
//        }
//        for (Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//
//        return "logout";
//    }


}