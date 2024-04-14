package com.gersondeveloper.cadastroavd2024.domain.entities;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
}
