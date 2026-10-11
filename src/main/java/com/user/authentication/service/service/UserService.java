package com.user.authentication.service.service;

import com.user.authentication.service.dto.RequestUserDto;
import com.user.authentication.service.dto.ResponseUserDto;

public interface UserService {

    ResponseUserDto createUser(RequestUserDto requestUserDto);
}
