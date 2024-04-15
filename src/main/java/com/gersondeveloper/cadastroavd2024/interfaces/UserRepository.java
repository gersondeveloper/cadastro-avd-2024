package com.gersondeveloper.cadastroavd2024.interfaces;

import com.gersondeveloper.cadastroavd2024.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);
}
