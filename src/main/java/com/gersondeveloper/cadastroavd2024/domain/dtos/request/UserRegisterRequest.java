package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(
    @NotBlank @Email String email,
    @NotBlank String name,
    @NotBlank String password,
    @NotNull UserRole role) {}
