package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import org.springframework.security.core.userdetails.UserDetails;

public record UserAuthenticationResponseDto(UserDetails userDetails, String token) {
}
