package com.gersondeveloper.cadastroavd2024.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse toUserResponse(User user);

  List<UserResponse> toUserResponseList(List<User> userList);

  User toUser(UserResponse userResponse);

  List<User> toUserList(List<UserResponse> userResponseList);
}
