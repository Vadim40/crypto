package com.example.wallet.Client;

import com.example.wallet.Configurations.FeignConfig;
import com.example.wallet.Models.DTO.AccountResponse;
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
