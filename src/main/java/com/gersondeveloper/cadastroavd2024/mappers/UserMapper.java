package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface UserMapper {
    public User toUser(UserResponseDto userResponseDto);
    public User toUser(UserDetails userDetails);
}
