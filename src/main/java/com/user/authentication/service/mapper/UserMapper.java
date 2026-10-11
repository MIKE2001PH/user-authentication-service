package com.user.authentication.service.mapper;

import com.user.authentication.service.dto.PhoneDto;
import com.user.authentication.service.dto.RequestUserDto;
import com.user.authentication.service.dto.ResponseUserDto;
import com.user.authentication.service.entity.CustomerEntity;
import com.user.authentication.service.entity.PhoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    @Mapping(target = "lastlogin", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "isactive", ignore = true)
    CustomerEntity requestToEntity(RequestUserDto request);

    @Mapping(target = "lastlogin", source = "lastLogin")
    ResponseUserDto entityToResponseDto(CustomerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "citycode", source = "cityCode")
    @Mapping(target = "countrycode", source = "countryCode")
    PhoneEntity phoneDtoToEntity(PhoneDto phoneDto);

    @Mapping(target = "cityCode", source = "citycode")
    @Mapping(target = "countryCode", source = "countrycode")
    PhoneDto phoneEntityToDto(PhoneEntity phoneEntity);
}
