package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.CustomUserDetails;
import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Services.AccountServiceImpl;
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
    private final AccountServiceImpl accountService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final OtpService otpService;



    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            log.warn("authentication failed: invalid credential for user: {}", authRequest.getEmail());
            return new ResponseEntity<>("wrong password or login", HttpStatus.UNAUTHORIZED);
        }
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authRequest.getEmail());
        if(userDetails.isOTPEnabled()){
            otpService.generateOtp(userDetails.getUsername());
            String token = jwtTokenUtils.generateTokenOtp(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        }
        else {
            String token = jwtTokenUtils.generateToken(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        }
    }
//    @PostMapping("verify-otp")
//    public ResponseEntity<Object> verifyOtp(@RequestParam String otp){
//        Account account= userDetailsService.getAuthenticatedUser();
//        if(!otpService.verifyOtp(otp,account.getId())){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        String token = jwtTokenUtils.generateToken(userDetails);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
//    }
    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String otp) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String jwtToken = authorizationHeader.substring(7); // Удаление префикса "Bearer "
            String email = jwtTokenUtils.getUsername(jwtToken);
            if (email == null) {
                return new ResponseEntity<>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
            if (otpService.verifyOtp(otp,email)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String finalToken = jwtTokenUtils.generateToken(userDetails);
                return new ResponseEntity<>(new JwtResponse(finalToken), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid OTP", HttpStatus.UNAUTHORIZED);
            }
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