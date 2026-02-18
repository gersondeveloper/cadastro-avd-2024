package com.gersondeveloper.cadastroavd2024.domain.entities.enums;

import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN("admin"),
  USER("user"),
  CUSTOMER("customer");

  private final String role;

  UserRole(String role) {
    this.role = role;
  }
}
