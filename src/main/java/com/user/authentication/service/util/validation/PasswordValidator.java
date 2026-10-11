package com.user.authentication.service.util.validation;

import com.user.authentication.service.util.ValidPassword;
import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // Lee la expresión regular desde application.properties (con un valor por defecto si no existe)
    @Value("${app.security.password-regex:^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$}")
    private String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return Pattern.matches(regex, password);
    }
}