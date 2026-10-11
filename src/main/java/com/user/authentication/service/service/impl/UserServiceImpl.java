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
import com.user.authentication.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper mapper;

    @Override
    public ResponseUserDto createUser(RequestUserDto requestUserDto) {
         if (userRepository.existsByEmail(requestUserDto.getEmail())) {
                throw new UserAlreadyExistsException("El correo electrónico ya se encuentra registrado");
            }

            CustomerEntity nuevoUsuario = new CustomerEntity();
            String token = jwtService.generarToken(requestUserDto.getEmail());
            List<PhoneEntity> phonesList = new ArrayList<>();

            for (PhoneDto phoneDto: requestUserDto.getPhones()){
                PhoneEntity phoneEntity = new PhoneEntity();

                phoneEntity.setCitycode(phoneDto.getCityCode());
                phoneEntity.setCountrycode(phoneDto.getCountryCode());
                phoneEntity.setNumber(phoneDto.getNumber());

                phonesList.add(phoneEntity);
            }

            nuevoUsuario.setName(requestUserDto.getName());
            nuevoUsuario.setEmail(requestUserDto.getEmail());
            nuevoUsuario.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
            nuevoUsuario.setToken(token);
            nuevoUsuario.setPhones(phonesList);
            nuevoUsuario.setIsactive(true);

            return mapper.entityToResponseDto(userRepository.save(nuevoUsuario));
    }
}
