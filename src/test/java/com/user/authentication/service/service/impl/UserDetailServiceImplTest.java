package com.user.authentication.service.service.impl;

import com.user.authentication.service.entity.CustomerEntity;
import com.user.authentication.service.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    @DisplayName("Debe cargar los detalles de usuario correctamente cuando el usuario existe")
    void loadUserByUsername_Success() {
        String email = "juan.perez@dominio.cl";
        CustomerEntity customer = CustomerEntity.builder()
                .email(email)
                .password("encoded-password")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException cuando el usuario no existe")
    void loadUserByUsername_NotFound_ThrowsException() {
        String email = "noexiste@dominio.cl";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailService.loadUserByUsername(email)
        );

        assertTrue(exception.getMessage().contains(email));
        verify(userRepository, times(1)).findByEmail(email);
    }
}
