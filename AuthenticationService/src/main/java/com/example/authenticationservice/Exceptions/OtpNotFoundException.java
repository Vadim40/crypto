package com.example.authenticationservice.Exceptions;

public class OtpNotFoundException extends RuntimeException{
    public OtpNotFoundException(String message){
        super(message);
    }
}
