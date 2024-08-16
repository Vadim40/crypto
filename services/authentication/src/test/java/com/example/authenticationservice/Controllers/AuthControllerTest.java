package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.CustomUserDetails;
import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Services.CustomUserDetailsService;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Services.Interfaces.AuthenticationService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import com.example.authenticationservice.Utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;
    @Autowired
    private OtpService otpService;

    @Autowired
   private  JwtTokenUtils jwtTokenUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        accountService.deleteAllAccounts();
    }


    @Test
    void returnTokenTest_OK() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        accountService.saveAccount(account);
        JwtRequest jwtRequest = new JwtRequest(email, password);

        String jsonRequest = objectMapper.writeValueAsString(jwtRequest);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void returnTokenOtpTest_OK() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        account.setOtpEnabled(true);
        accountService.saveAccount(account);
        JwtRequest jwtRequest = new JwtRequest(email, password);

        String jsonRequest = objectMapper.writeValueAsString(jwtRequest);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(jsonResponse, JwtResponse.class);
        String jwtToken = jwtResponse.getToken();

        boolean isOtpToken = jwtTokenUtils.isOtpToken(jwtToken);
        Assertions.assertThat(isOtpToken).isTrue();

    }

    @Test
    void returnTokenTest_Unauthorized() throws Exception {
        String email = "some";
        String password = "1234";
        JwtRequest jwtRequest = new JwtRequest(email, password);

        String jsonRequest = objectMapper.writeValueAsString(jwtRequest);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void verifyOtp_Ok() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        account.setOtpEnabled(true);
        accountService.saveAccount(account);

        String otp = otpService.generateOtp(email);

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String jwtOtp = jwtTokenUtils.generateTokenOtp(customUserDetails);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify-otp")
                        .param("otp", otp)
                        .header("Authorization", "Bearer " + jwtOtp))
                .andExpect(status().isOk());


    }

    @Test
    void verifyOtp__OtpIsNotEnabled() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        accountService.saveAccount(account);

        String otp = "1222";

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String jwtOtp = jwtTokenUtils.generateTokenOtp(customUserDetails);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify-otp")
                        .param("otp", otp)
                        .header("Authorization", "Bearer " + jwtOtp))
                .andExpect(status().isBadRequest());


    }

    @Test
    void verifyOtp_Forbidden_NotTokenOtp() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        account.setOtpEnabled(true);
        accountService.saveAccount(account);

        String otp = otpService.generateOtp(email);

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String jwt = jwtTokenUtils.generateToken(customUserDetails);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify-otp")
                        .param("otp", otp)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isForbidden());


    }
}