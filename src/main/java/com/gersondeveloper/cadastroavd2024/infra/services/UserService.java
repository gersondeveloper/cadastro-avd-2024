package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.UserRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    EmailService emailService;

    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        repository.save(user);
    }

    public List<UserResponseDto> findAllByRole(UserRole role) {
        return repository.findAllByRole(role);
    }

    public UserDetails findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void changePassword(User user, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setActive(true);  // or other logic
        save(user);
    }

    public void setUserActive(User user, boolean active) {
        user.setActive(active);
        save(user);
    }

    public User registerNewUser (UserRegisterRequestDto user) {
        if (findByEmail(user.email()) != null) {
            throw new ValidationException("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.password());
        User newUser = User.builder()
                .email(user.email())
                .name(user.name())
                .contactName(user.name())
                .password(encodedPassword)
                .role(user.role())
                .build();
        save(newUser);
        return newUser;
    }

    public void sendConfirmationEmail(User user, String confirmToken) {
        emailService.sendTokenEmail(user.getEmail(), confirmToken);
    }
}
