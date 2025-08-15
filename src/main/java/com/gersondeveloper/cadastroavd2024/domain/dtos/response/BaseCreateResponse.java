package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

public record BaseCreateResponse(
        Long id,
        String message,
        boolean ok,
        String url) {
}
