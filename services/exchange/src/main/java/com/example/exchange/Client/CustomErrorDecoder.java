package com.example.exchange.Client;

import com.example.exchange.Exceptions.BusinessException;
import com.example.exchange.Models.DTOs.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            String message = extractMessageFromBody(response);
            return new BusinessException(message);
        }
        return defaultDecoder.decode(methodKey, response);
    }

    private String extractMessageFromBody(Response response) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            ErrorResponse errorResponse = objectMapper.readValue(body.toString(), ErrorResponse.class);
            return errorResponse.message();
        } catch (IOException e) {
            log.error("IOException occurred while reading response body: {}", e.getMessage(), e);
            return "An error occurred while processing the response.";
        }
    }
}
