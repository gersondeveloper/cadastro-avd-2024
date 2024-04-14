package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import java.util.Date;

public record CustomerRequestDto(
        String name,
        String phone,
        String email,
        Date creationDate,
        String createdBy,
        Date modificationDate,
        String modifiedBy) {
}



