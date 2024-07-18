package com.example.walletservice.Validators;

import com.example.walletservice.Validators.Annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecialCharacter = password.matches(".*[!@#$%^&*()].*");

        if (!hasUppercase) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter").addConstraintViolation();
        }
        if (!hasLowercase) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter").addConstraintViolation();
        }
        if (!hasDigit) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit").addConstraintViolation();
        }
        if (!hasSpecialCharacter) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one special character").addConstraintViolation();
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialCharacter;
    }
}