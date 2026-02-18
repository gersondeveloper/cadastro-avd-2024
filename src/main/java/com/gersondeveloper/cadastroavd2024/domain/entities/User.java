package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

  @NotBlank
  @Column(unique = true)
  @Email
  private String email;

  @NotBlank private String name;
  private String contactName;
  private String password;

  @Enumerated(EnumType.ORDINAL)
  private UserRole role;

  private String phone;

  @Column(name = "enabled", nullable = false, columnDefinition = "boolean default false")
  private boolean enabled;

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.creationDate = LocalDateTime.now();

      if (this.phone == null) {
        this.phone = "";
      }
      if (this.password == null || this.password.isBlank()) {
        this.password = "change_the_password";
      }
    }
  }

  @Override
  public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role == UserRole.ADMIN) {
      return List.of(
          new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    } else if (this.role == UserRole.CUSTOMER) {
      return List.of(
          new SimpleGrantedAuthority("ROLE_CUSTOMER"), new SimpleGrantedAuthority("ROLE_USER"));
    }
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public @NonNull String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
