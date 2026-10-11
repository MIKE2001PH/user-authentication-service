package com.user.authentication.service.controller;

import com.user.authentication.service.dto.LoginRequest;
import com.user.authentication.service.dto.RequestUserDto;
import com.user.authentication.service.dto.ResponseUserDto;
import com.user.authentication.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<ResponseUserDto> createUser(@Valid @RequestBody RequestUserDto request){
        return ResponseEntity.ok().body(userService.createUser(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Boolean> autenticarUsuario(@Valid @RequestBody LoginRequest request) {
     Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

     if (authentication.isAuthenticated()){
         return ResponseEntity.ok().body(Boolean.TRUE);
     }

        return ResponseEntity.ok().body(Boolean.FALSE);
    }
}
