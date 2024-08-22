package com.example.exchange.Client;


import com.example.exchange.Config.FeignConfig;
import com.example.exchange.Models.DTOs.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "authentication-service",
        url = "${application.config.authentication-url}",
        configuration = FeignConfig.class
)
public interface AuthenticationClient {
    @GetMapping("/email/{email}")
    AccountResponse findAccountByEmail(@PathVariable("email") String email);
}
