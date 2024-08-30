package com.example.authenticationservice.Models.DTOs;

import com.example.authenticationservice.Validators.Annotations.UniqueEmail;
import com.example.authenticationservice.Validators.Annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record AccountCreationRequest(
        @ValidPassword
        String password,
        @Email(message = "The email address format is incorrect")
        @UniqueEmail
        String email
) {

}
