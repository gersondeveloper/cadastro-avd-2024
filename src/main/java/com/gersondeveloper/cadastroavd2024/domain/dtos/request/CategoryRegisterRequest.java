package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRegisterRequest(
    @NotBlank String name, @NotBlank String description, boolean active) {}
