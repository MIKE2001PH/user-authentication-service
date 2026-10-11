package com.user.authentication.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.authentication.service.dto.PhoneDto;
import com.user.authentication.service.dto.RequestUserDto;
import com.user.authentication.service.dto.ResponseUserDto;
import com.user.authentication.service.exception.GlobalExceptionHandler;
import com.user.authentication.service.exception.UserAlreadyExistsException;
import com.user.authentication.service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
    }

    private RequestUserDto createValidRequest() {
        PhoneDto phone = PhoneDto.builder()
                .number("1234567")
                .cityCode("1")
                .countryCode("57")
                .build();

        return RequestUserDto.builder()
                .name("Juan Perez")
                .email("juan.perez@dominio.cl")
                .password("Password123")
                .phones(List.of(phone))
                .build();
    }

    @Test
    @DisplayName("Debe registrar exitosamente un usuario y retornar HTTP 200 OK")
    void createUser_Success() throws Exception {
        RequestUserDto request = createValidRequest();

        ResponseUserDto response = ResponseUserDto.builder()
                .id(UUID.randomUUID())
                .name("Juan Perez")
                .email("juan.perez@dominio.cl")
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastlogin(LocalDateTime.now())
                .token("jwt-sample-token")
                .isactive(true)
                .phones(request.getPhones())
                .build();

        when(userService.createUser(any(RequestUserDto.class))).thenReturn(response);

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Perez"))
                .andExpect(jsonPath("$.email").value("juan.perez@dominio.cl"))
                .andExpect(jsonPath("$.token").value("jwt-sample-token"));

        verify(userService, times(1)).createUser(any(RequestUserDto.class));
    }

    @Test
    @DisplayName("Debe retornar HTTP 409 Conflict cuando el usuario ya existe")
    void createUser_UserAlreadyExists_ReturnsConflict() throws Exception {
        RequestUserDto request = createValidRequest();

        when(userService.createUser(any(RequestUserDto.class)))
                .thenThrow(new UserAlreadyExistsException("El correo electrónico ya se encuentra registrado"));

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo electrónico ya se encuentra registrado"));

        verify(userService, times(1)).createUser(any(RequestUserDto.class));
    }

    @Test
    @DisplayName("Debe retornar HTTP 400 Bad Request cuando el formato del email es inválido")
    void createUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        RequestUserDto request = createValidRequest();
        request.setEmail("email-invalido");

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Debe retornar HTTP 400 Bad Request cuando el nombre está vacío")
    void createUser_EmptyName_ReturnsBadRequest() throws Exception {
        RequestUserDto request = createValidRequest();
        request.setName("");

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Debe retornar HTTP 400 Bad Request cuando el password no cumple con el formato")
    void createUser_InvalidPassword_ReturnsBadRequest() throws Exception {
        RequestUserDto request = createValidRequest();
        request.setPassword("simple"); // no tiene mayúscula ni número y tiene menos de 8 caracteres

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Debe retornar HTTP 400 Bad Request cuando la lista de teléfonos está vacía")
    void createUser_EmptyPhones_ReturnsBadRequest() throws Exception {
        RequestUserDto request = createValidRequest();
        request.setPhones(Collections.emptyList());

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        verify(userService, never()).createUser(any());
    }
}
