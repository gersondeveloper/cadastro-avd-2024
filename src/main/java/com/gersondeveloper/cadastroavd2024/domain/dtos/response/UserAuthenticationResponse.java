package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import org.springframework.security.core.userdetails.UserDetails;

public record UserAuthenticationResponse(UserDetails userDetails, String token) {
}
