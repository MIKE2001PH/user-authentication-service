package com.user.authentication.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Campo email no puede ser vacío.")
    private String email;
    @NotBlank(message = "Campo password no puede ser vacío.")
    private String password;

}
