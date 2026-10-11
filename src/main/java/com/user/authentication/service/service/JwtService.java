package com.user.authentication.service.service;

public interface JwtService {

    String generarToken(String email);
    String extraerEmail(String token);
    boolean esTokenValido(String token, String emailUsuario);
}
