package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import com.gersondeveloper.cadastroavd2024.domain.entities.user.UserRole;

public record UserRegisterRequestDto(
        String email,
        String name,
        String password,
        UserRole role) {
}
