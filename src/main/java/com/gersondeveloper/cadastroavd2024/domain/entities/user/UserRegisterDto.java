package com.gersondeveloper.cadastroavd2024.domain.entities.user;

public record UserRegisterDto(
        String login,
        String password,
        UserRole role) {
}
