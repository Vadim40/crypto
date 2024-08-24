//package com.example.wallet.Controllers;
//
//import com.example.wallet.Models.DTO.AccountResponse;
//import com.example.wallet.Models.DTO.TransferRequest;
//import com.example.wallet.Models.Enum.TransactionType;
//import com.example.wallet.Models.Transaction;
//import com.example.wallet.Models.Wallet;
//import com.example.wallet.Repositories.TransactionRepository;
//import com.example.wallet.Services.Interfaces.AccountService;
//import com.example.wallet.Services.Interfaces.TokenService;
//import com.example.wallet.Services.Interfaces.WalletService;
//import com.example.wallet.WalletApplication;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = WalletApplication.class)
//@AutoConfigureMockMvc
//@Transactional
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//public class TransactionControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private String jwtToken;
//    private Long mainAccountId;
//    private Long secondAccountId;
//
//
//    @Autowired
//    private AccountService accountService;
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private TokenService tokenService;
//
//    @Autowired
//    private WalletService walletService;
//
//
////    @BeforeAll
////    void setUp() {
////
////        String registrationUrl = "http://localhost:8090/api/v1/account/sign-up";
////        AccountCreationRequest accountRequest = new AccountCreationRequest("Password1!", "testuser@example.com");
////        restTemplate.postForEntity(registrationUrl, accountRequest, Void.class);
////       AccountCreationRequest accountRequest2= new AccountCreationRequest("Password1!", "another@example.com");
////        restTemplate.postForEntity(registrationUrl, accountRequest2, Void.class);
////        String authUrl = "http://localhost:8090/api/v1/auth";
////        JwtRequest jwtRequest = new JwtRequest("Password1!", "testuser@example.com");
////        ResponseEntity<JwtResponse> authResponse = restTemplate.postForEntity(authUrl, jwtRequest, JwtResponse.class);
////
////        if (authResponse.getStatusCode().is2xxSuccessful()) {
////            jwtToken = authResponse.getBody().token();
////        }
////        System.out.println(jwtToken);
////        mainAccountId =accountService.getAccountByUsername("testuser@example.com").id();
////        secondAccountId=accountService.getAccountByUsername("another@example.com").id();
////    }
//
//    @Test
//    void findTransactionByIdTest() throws Exception {
//        Transaction transaction=new Transaction();
//        transaction.setAccountId(mainAccountId);
//        transaction.setAmount(BigDecimal.TEN);
//        transaction.setTransactionDate(LocalDate.now());
//        transaction.setTransactionType(TransactionType.RECEIVE);
//        transaction.setTokenSymbol("USD");
//        Long transactionId =transactionRepository.save(transaction).getId();
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/{id}", transactionId)
//                .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", roles = "USER")
//    void transferTokens_OK() throws Exception {
//        Wallet wallet=walletService.findWalletByAccountId(mainAccountId);
//        tokenService.addTokens("USD", BigDecimal.TEN, wallet);
//        String destinationAddress=walletService.findWalletByAccountId(secondAccountId).getAddress();
//        TransferRequest transferRequest= new TransferRequest(destinationAddress,"USD", BigDecimal.ONE );
//        String request= objectMapper.writeValueAsString(transferRequest);
//
//        when(accountService.getCurrentAccount()).thenReturn(new AccountResponse(1l, "some"));
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/transfer-tokens")
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk());
//    }
//
//
//
//}
