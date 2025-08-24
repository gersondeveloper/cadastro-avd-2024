package com.gersondeveloper.cadastroavd2024.domain.interfaces;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CustomerResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    List<CustomerResponseDto> findAllByRole(UserRole role);
}
