package com.gersondeveloper.cadastroavd2024.domain.entities;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Email
    private String email;

    @NotBlank
    private String name;

    private String contactName;

    @NotBlank
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    private String phone;
    private boolean isActive = true;

    @PrePersist
    public void prePersist(){
        if (this.id == null) {
            this.creationDate = LocalDateTime.now();
            this.isActive = true;
        }
    }

    private LocalDateTime creationDate;
    private String createdBy;
    private LocalDateTime modificationDate;
    private String modifiedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else if (this.role == UserRole.CUSTOMER) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
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
        return true;
    }
}
