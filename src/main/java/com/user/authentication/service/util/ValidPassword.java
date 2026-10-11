package com.user.authentication.service.util;

import com.user.authentication.service.util.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "La clave no cumple con el formato requerido. Contraseña debe incluir almenos una minúscula, una mayúscula, un número y mínimo 8 caracteres";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}