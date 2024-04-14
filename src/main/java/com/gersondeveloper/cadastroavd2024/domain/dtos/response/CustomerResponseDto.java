package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import com.gersondeveloper.cadastroavd2024.domain.entities.Customer;

import java.time.LocalDateTime;

public record CustomerResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        boolean isActive,
        LocalDateTime creationDate,
        String createdBy,
        LocalDateTime modificationDate,
        String modifiedBy) {
    public CustomerResponseDto(Customer customer) {
        this(customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.isActive(),
                customer.getCreationDate(),
                customer.getCreatedBy(),
                customer.getModificationDate(),
                customer.getModifiedBy());
    }
}
