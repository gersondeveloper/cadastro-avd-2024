package com.gersondeveloper.cadastroavd2024.domain.interfaces;

import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
    Page<User> findAllByRole(UserRole role, Pageable pageable);
}
