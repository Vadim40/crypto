package com.example.authenticationservice.Validators;

import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Validators.Annotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private  final AccountService accountService;



    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return isUsernameUnique(name);
    }

    private boolean isUsernameUnique(String email) {
        return !accountService.isEmailExists(email);
    }
}