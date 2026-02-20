package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import java.time.LocalDateTime;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;

public record UserRegisterResponse(
    long id,
    String name,
    String email,
    String contactName,
    String phone,
    UserRole role,
    LocalDateTime creationDate,
    boolean isEnabled) {}
