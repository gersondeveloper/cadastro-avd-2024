package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;

import java.time.LocalDateTime;

public record CustomerResponseDto(long id, String name, String email, String contactName, String phone, UserRole role, LocalDateTime creationDate, boolean isActive) {
}
