package com.example.authenticationservice.Exceptions;

public class OTPNotFoundException extends RuntimeException{
    public OTPNotFoundException(String message){
        super(message);
    }
}
