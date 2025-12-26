package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.UserRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.ValidationException;
import com.gersondeveloper.cadastroavd2024.mappers.UserMapper;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserMapper mapper;

    @Observed(name="user.create")
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        LOGGER.info("creating user '{}'", user.getName());
        repository.save(user);
    }

    @Observed(name = "user.list-all")
    public List<UserResponseDto> findAll() {
        LOGGER.info("Listing all users");
        return mapper.toUserResponseDtoList(repository.findAll());
    }

    public List<UserResponseDto> findAllByRole(UserRole role) {
        return repository.findAllByRole(role);
    }

    @Observed(name="user.find-by-email")
    public UserDetails findByEmail(String email) {
        LOGGER.info("find {} user", email);
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

    @Observed(name="user.create")
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
        LOGGER.info("User {} created witth success!", newUser.getName());
        return newUser;
    }


}
