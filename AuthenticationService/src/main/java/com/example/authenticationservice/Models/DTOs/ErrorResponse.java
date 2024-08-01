package com.example.authenticationservice.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private String path;
}
