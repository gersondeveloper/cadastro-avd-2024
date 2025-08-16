package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;

public record CreateCustomerRequestDto (
        String email,
        String name,
        String password,
        UserRole role,
        String phone,
        String contactName
) {}
