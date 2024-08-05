package com.example.authenticationservice.Models.DTOs;

import com.example.authenticationservice.Validators.Annotations.UniqueEmail;
import com.example.authenticationservice.Validators.Annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @ValidPassword
    private String password;
    @Email
    @UniqueEmail
    private String email;
}
