package com.gersondeveloper.cadastroavd2024.infra.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.UserRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.ValidationException;
import com.gersondeveloper.cadastroavd2024.mappers.UserMapper;

import io.micrometer.observation.annotation.Observed;

@Service
public class UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private UserRepository repository;

  @Autowired UserMapper mapper;

  @Observed(name = "user.create")
  public void save(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
    LOGGER.info("creating user '{}'", user.getName());
    repository.save(user);
  }

  @Observed(name = "user.list-all")
  public List<UserResponse> findAll(PageRequest pageRequest) {
    LOGGER.info("Listing all users");
    return mapper.toUserResponseList(repository.findAll(pageRequest).getContent());
  }

  public List<UserResponse> findAllByRole(UserRole role, PageRequest pageRequest) {
    return mapper.toUserResponseList(repository.findAllByRole(role, pageRequest).getContent());
  }

  @Observed(name = "user.find-by-email")
  public UserDetails findByEmail(String email) {
    LOGGER.info("find {} user", email);
    return repository.findByEmail(email);
  }

  public void changePassword(User user, String rawPassword) {
    String encodedPassword = passwordEncoder.encode(rawPassword);
    user.setPassword(encodedPassword);
    user.setEnabled(true); // or other logic
    save(user);
  }

  public void setUserActive(User user, boolean active) {
    user.setEnabled(active);
    save(user);
  }

  @Observed(name = "user.create")
  public User registerNewUser(UserRegisterRequest user) {
    if (findByEmail(user.email()) != null) {
      throw new ValidationException("User already exists");
    }
    String encodedPassword = passwordEncoder.encode(user.password());
    User newUser =
        User.builder()
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
