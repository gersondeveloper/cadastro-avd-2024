package com.gersondeveloper.cadastroavd2024.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserRegisterResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(source = "enabled", target = "isEnabled")
  UserRegisterResponse toUserResponse(User user);

  List<UserRegisterResponse> toUserResponseList(List<User> userList);

  User toUser(UserRegisterRequest request);

  List<User> toUserList(List<UserRegisterResponse> userRegisterResponseList);
}
