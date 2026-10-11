package com.user.authentication.service.dto;

import com.user.authentication.service.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserDto {

    @NotBlank(message = "Campo name no debe ser vacío.")
    private String name;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El formato del correo debe ser válido y tener un dominio")
    private String email;

    @NotBlank(message = "Campo password no debe ser vacío.")
    @ValidPassword
    private String password;

    @NotEmpty(message = "Debe incluir al menos un numero de telefono.")
    private List<PhoneDto> phones;
}
