package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateCustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CreateCustomerRequestDto toCreateCustomerRequestDto(User customer);
    User toUser(CreateCustomerRequestDto requestDto);
}
