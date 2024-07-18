package com.example.walletservice.Models.DTOs;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}