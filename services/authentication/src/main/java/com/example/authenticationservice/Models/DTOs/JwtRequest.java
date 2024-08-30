package com.example.authenticationservice.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record JwtRequest(
        String email,
        String password
) {

}