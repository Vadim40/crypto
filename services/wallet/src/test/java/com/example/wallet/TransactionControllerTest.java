package com.example.wallet;

import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import com.example.wallet.Repositories.TransactionRepository;
import com.example.wallet.Services.Interfaces.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();
    private String jwtToken;
    private Long accountId;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;


    @BeforeAll
    void setUp() {
        // per once
//        String registrationUrl = "http://localhost:8090/api/v1/account/sign-up";
//        AccountCreationRequest accountRequest = new AccountCreationRequest("Password1!", "testuser@example.com");
//        restTemplate.postForEntity(registrationUrl, accountRequest, Void.class);
        String authUrl = "http://localhost:8090/api/v1/auth";
        JwtRequest jwtRequest = new JwtRequest("Password1!", "testuser@example.com");
        ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(authUrl, jwtRequest, JwtResponse.class);

        if (authResponse.getStatusCode().is2xxSuccessful()) {
            jwtToken = authResponse.getBody().token();
        }
        System.out.println(jwtToken);
        accountId =accountService.getAccountByUsername("testuser@example.com").id();
    }

    @Test
    void findTransactionByIdTest() throws Exception {
        Transaction transaction=new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.RECEIVE);
        transaction.setTokenType("USD");
        Long transactionId =transactionRepository.save(transaction).getId();


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/{id}", transactionId)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }



}
