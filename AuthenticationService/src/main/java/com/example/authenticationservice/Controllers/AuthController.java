package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest) {

        String token = authenticationService.generateJwtBasedOnOtp(authRequest.getEmail());
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String otp) {

        String jwtToken = authorizationHeader.substring(7);
        authenticationService.verifyOtpAndGenerateJwt(jwtToken, otp);
        return new ResponseEntity<>(HttpStatus.OK);
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