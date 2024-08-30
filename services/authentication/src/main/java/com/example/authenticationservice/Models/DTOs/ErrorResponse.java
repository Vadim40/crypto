package com.example.authenticationservice.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record ErrorResponse(
        String error,
        String message,
         int status,
         String path
) {

}
