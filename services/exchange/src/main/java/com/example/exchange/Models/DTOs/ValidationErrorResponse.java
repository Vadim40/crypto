package com.example.exchange.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private Map<String, List<String>> errors;

    public ValidationErrorResponse(BindingResult bindingResult) {
        this.errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = getErrorFieldName(error);
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
    }


    private String getErrorFieldName(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField();
        } else {
            return error.getObjectName();
        }
    }
}