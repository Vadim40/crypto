package com.example.authenticationservice.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


public record JwtResponse(
        Map<String, String> tokens
) {

}