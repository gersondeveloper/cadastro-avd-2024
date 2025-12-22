package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserResponseDto toUserResponseDto(User user);
    public List<UserResponseDto> toUserResponseDtoList(List<User> userList);
}
