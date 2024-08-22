package com.example.exchange.Handling;

import com.example.exchange.Exceptions.CryptoRateNotFoundException;
import com.example.exchange.Models.DTOs.ErrorResponse;
import com.example.exchange.Models.DTOs.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(ex.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Error",
                validationErrorResponse.getErrors().toString(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        log.warn("Validation errors: {}", validationErrorResponse.getErrors());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CryptoRateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(CryptoRateNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Account Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );
        log.warn(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
