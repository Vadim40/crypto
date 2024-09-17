package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Kafka.DTOs.OtpVerification;
import com.example.authenticationservice.Kafka.DTOs.UserLoginEvent;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.DTOs.JwtRequest;
import com.example.authenticationservice.Models.DTOs.JwtResponse;
import com.example.authenticationservice.Models.DTOs.RefreshRequest;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import com.example.authenticationservice.Services.RefreshTokenServiceImpl;
import com.example.authenticationservice.Utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.datasource.platform=h2",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.kafka.bootstrap-servers=${kafka.bootstrap-servers}"
})
class AuthControllerTest {
    private static KafkaContainer kafkaContainer;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;
    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    private final String url = "/api/v1/auth";

    @BeforeAll
    static void setUp() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
        System.setProperty("kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
    }

    @AfterAll
    static void tearDown() {
        if (kafkaContainer != null) {
            kafkaContainer.stop();
        }
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


        mockMvc.perform(MockMvcRequestBuilders.post(url + "/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(kafkaTemplate).send(Mockito.argThat((Message<?> message) ->
                message.getHeaders().get(KafkaHeaders.TOPIC).equals("user-login-event") &&
                        message.getPayload() instanceof UserLoginEvent
        ));


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


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url + "/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(jsonResponse, JwtResponse.class);
        String jwtToken = jwtResponse.tokens().get("accessTokenOtp");

        boolean isOtpToken = jwtTokenUtils.isOtpToken(jwtToken);
        Assertions.assertThat(isOtpToken).isTrue();

        Mockito.verify(kafkaTemplate).send(Mockito.argThat((Message<?> message) ->
                message.getHeaders().get(KafkaHeaders.TOPIC).equals("otp-verification") &&
                        message.getPayload() instanceof OtpVerification
        ));

    }

    @Test
    void returnTokenTest_Unauthorized() throws Exception {
        String email = "some";
        String password = "1234";
        JwtRequest jwtRequest = new JwtRequest(email, password);

        String jsonRequest = objectMapper.writeValueAsString(jwtRequest);


        mockMvc.perform(MockMvcRequestBuilders.post(url + "/login")
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


        mockMvc.perform(MockMvcRequestBuilders.post(url + "/verify-otp")
                        .param("otp", otp)
                        .header("X-User-Name", email)
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isOk());

        Mockito.verify(kafkaTemplate).send(Mockito.argThat((Message<?> message) ->
                message.getHeaders().get(KafkaHeaders.TOPIC).equals("user-login-event") &&
                        message.getPayload() instanceof UserLoginEvent
        ));


    }

    @Test
    void verifyOtp_OtpIsNotEnabled() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        accountService.saveAccount(account);

        String otp = "1222";
        mockMvc.perform(MockMvcRequestBuilders.post(url + "/verify-otp")
                        .param("otp", otp)
                        .header("X-User-Name", email)
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isBadRequest());


    }

    @Test
    void logout_OK() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        accountService.saveAccount(account);
        JwtRequest jwtRequest = new JwtRequest(email, password);

        String jsonRequest = objectMapper.writeValueAsString(jwtRequest);


        mockMvc.perform(MockMvcRequestBuilders.post(url + "/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Account foundAccount1 = accountService.findAccountByEmail(email);
        System.out.println(foundAccount1.getRefreshToken().getTokenHash());
        String refreshToken1=foundAccount1.getRefreshToken().getTokenHash();
        Assertions.assertThat(refreshToken1).isNotNull();

        mockMvc.perform(MockMvcRequestBuilders.post(url + "/logout")
                        .header("X-User-Name", email)
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isOk());
        Account foundAccount2 = accountService.findAccountByEmail(email);

        Assertions.assertThat(foundAccount2.getRefreshToken()).isNull();
    }
    @Test
    void refreshToken_Ok() throws Exception {
        Account account = new Account();
        String email = "some";
        String password = "1234";
        account.setEmail(email);
        account.setPassword(password);
        accountService.saveAccount(account);
        String refreshToken=refreshTokenService.createRefreshToken(email);
        RefreshRequest request=new RefreshRequest(refreshToken);
        String jsonRequest=objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(url + "/refresh-token")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}