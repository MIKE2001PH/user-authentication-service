package com.user.authentication.service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
    }

    @Test
    @DisplayName("Debe generar un token JWT no nulo para un email válido")
    void generarToken_Success() {
        String email = "juan.perez@dominio.cl";
        String token = jwtService.generarToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Debe extraer correctamente el email (subject) del token generado")
    void extraerEmail_Success() {
        String email = "juan.perez@dominio.cl";
        String token = jwtService.generarToken(email);

        String extractedEmail = jwtService.extraerEmail(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Debe validar exitosamente que el token pertenece al email del usuario")
    void esTokenValido_Success() {
        String email = "juan.perez@dominio.cl";
        String token = jwtService.generarToken(email);

        boolean esValido = jwtService.esTokenValido(token, email);

        assertTrue(esValido);
    }

    @Test
    @DisplayName("Debe retornar false si el token no pertenece al email del usuario")
    void esTokenValido_WrongEmail_ReturnsFalse() {
        String email = "juan.perez@dominio.cl";
        String token = jwtService.generarToken(email);

        boolean esValido = jwtService.esTokenValido(token, "otro.usuario@dominio.cl");

        assertFalse(esValido);
    }
}
