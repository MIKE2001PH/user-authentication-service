package com.user.authentication.service.service.impl;

import com.user.authentication.service.dto.PhoneDto;
import com.user.authentication.service.dto.RequestUserDto;
import com.user.authentication.service.dto.ResponseUserDto;
import com.user.authentication.service.entity.CustomerEntity;
import com.user.authentication.service.entity.PhoneEntity;
import com.user.authentication.service.exception.UserAlreadyExistsException;
import com.user.authentication.service.mapper.UserMapper;
import com.user.authentication.service.repository.UserRepository;
import com.user.authentication.service.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private RequestUserDto requestUserDto;

    @BeforeEach
    void setUp() {
        PhoneDto phone1 = PhoneDto.builder()
                .number("1234567")
                .cityCode("1")
                .countryCode("57")
                .build();

        PhoneDto phone2 = PhoneDto.builder()
                .number("7654321")
                .cityCode("2")
                .countryCode("56")
                .build();

        requestUserDto = RequestUserDto.builder()
                .name("Juan Perez")
                .email("juan.perez@dominio.cl")
                .password("Password123")
                .phones(List.of(phone1, phone2))
                .build();
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario exitosamente con contraseña encriptada y token generado")
    void createUser_Success() {
        when(userRepository.existsByEmail(requestUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password-xyz");
        when(jwtService.generarToken(requestUserDto.getEmail())).thenReturn("mocked-jwt-token-123");

        CustomerEntity savedEntity = CustomerEntity.builder()
                .id(UUID.randomUUID())
                .name("Juan Perez")
                .email("juan.perez@dominio.cl")
                .password("encoded-password-xyz")
                .token("mocked-jwt-token-123")
                .isactive(true)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastlogin(LocalDateTime.now())
                .build();

        when(userRepository.save(any(CustomerEntity.class))).thenReturn(savedEntity);

        ResponseUserDto expectedResponse = ResponseUserDto.builder()
                .id(savedEntity.getId())
                .name(savedEntity.getName())
                .email(savedEntity.getEmail())
                .token(savedEntity.getToken())
                .isactive(true)
                .created(savedEntity.getCreated())
                .modified(savedEntity.getModified())
                .lastlogin(savedEntity.getLastlogin())
                .phones(requestUserDto.getPhones())
                .build();

        when(mapper.entityToResponseDto(savedEntity)).thenReturn(expectedResponse);

        ResponseUserDto response = userService.createUser(requestUserDto);

        assertNotNull(response);
        assertEquals("Juan Perez", response.getName());
        assertEquals("juan.perez@dominio.cl", response.getEmail());
        assertEquals("mocked-jwt-token-123", response.getToken());
        assertTrue(response.isActive());

        // Verificar el comportamiento y datos guardados en el repositorio
        ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(userRepository, times(1)).save(entityCaptor.capture());
        CustomerEntity capturedCustomer = entityCaptor.getValue();

        assertEquals("Juan Perez", capturedCustomer.getName());
        assertEquals("juan.perez@dominio.cl", capturedCustomer.getEmail());
        assertEquals("encoded-password-xyz", capturedCustomer.getPassword());
        assertEquals("mocked-jwt-token-123", capturedCustomer.getToken());
        assertTrue(capturedCustomer.isActive());
        assertEquals(2, capturedCustomer.getPhones().size());

        PhoneEntity firstPhone = capturedCustomer.getPhones().get(0);
        assertEquals("1234567", firstPhone.getNumber());
        assertEquals("1", firstPhone.getCitycode());
        assertEquals("57", firstPhone.getCountrycode());

        verify(userRepository, times(1)).existsByEmail(requestUserDto.getEmail());
        verify(passwordEncoder, times(1)).encode("Password123");
        verify(jwtService, times(1)).generarToken(requestUserDto.getEmail());
        verify(mapper, times(1)).entityToResponseDto(savedEntity);
    }

    @Test
    @DisplayName("Debe lanzar UserAlreadyExistsException si el email ya existe y no guardar")
    void createUser_WhenUserAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(requestUserDto.getEmail())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(requestUserDto)
        );

        assertEquals("El correo electrónico ya se encuentra registrado", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(requestUserDto.getEmail());
        verify(userRepository, never()).save(any(CustomerEntity.class));
        verify(passwordEncoder, never()).encode(any());
        verify(jwtService, never()).generarToken(any());
        verify(mapper, never()).entityToResponseDto(any());
    }
}
