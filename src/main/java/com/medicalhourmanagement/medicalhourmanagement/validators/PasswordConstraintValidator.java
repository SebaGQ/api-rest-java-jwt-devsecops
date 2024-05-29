package com.medicalhourmanagement.medicalhourmanagement.validators;


import com.medicalhourmanagement.medicalhourmanagement.auth.constraints.PasswordConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        // La contraseña debe tener al menos 6 caracteres y contener números y letras
        return password.length() >= 6 && password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$");
    }
}