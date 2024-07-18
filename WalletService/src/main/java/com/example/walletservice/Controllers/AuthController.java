package com.example.walletservice.Controllers;

import com.example.walletservice.Models.DTOs.JwtRequest;
import com.example.walletservice.Models.DTOs.JwtResponse;
import com.example.walletservice.Services.AccountServiceImpl;
import com.example.walletservice.Services.CustomUserDetailsService;
import com.example.walletservice.Utils.JwtTokenUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AccountServiceImpl accountService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/auth")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            log.warn("authentication failed: invalid credential for user: {}", authRequest.getEmail());
            return new ResponseEntity<>("wrong password or login", HttpStatus.UNAUTHORIZED);
        }
        System.out.println(123134);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        System.out.println(123123);
        String token = jwtTokenUtils.generateToken(userDetails);
        System.out.println(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
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