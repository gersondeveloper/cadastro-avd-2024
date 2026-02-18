package com.gersondeveloper.cadastroavd2024.controllers;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/user")
@CrossOrigin(value = "http://localhost:4200")
public class UserController {

  @Autowired UserService userService;

  @Autowired TokenService tokenService;

  @Autowired EmailService emailService;

  @Observed(name = "user.register")
  @PostMapping(path = "/register", version = "v1")
  public ResponseEntity<UserCreateResponse> register(
      @RequestBody @Valid UserRegisterRequest data, UriComponentsBuilder ucb) {

    if (this.userService.findByEmail(data.email()) != null) {
      return getUserAlreadyexistsCreateResponseResponseEntity();
    }

    User newUser;
    try {
      newUser = this.userService.registerNewUser(data);
    } catch (DataAccessException ex) {
      return getBadRequestUserCreateResponseResponseEntity(ex);
    }
    String confirmToken = tokenService.generateToken(newUser);
    emailService.sendConfirmationEmail(newUser, confirmToken);

    String url = MessageFormat.format("/register/{0}", newUser.getId());
    UserCreateResponse response =
        new UserCreateResponse(201, "User created successfully!", true, url);
    URI locationOfNewUser = ucb.path("/register/{id}").buildAndExpand(newUser.getId()).toUri();
    return ResponseEntity.created(locationOfNewUser).body(response);
  }

  @Observed(name = "user.getAll")
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping(path = "/all", version = "v1")
  public ResponseEntity<List<UserResponse>> getAllUsers(
      @RequestParam UserRole role,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction,
      Pageable pageable) {
    if (role.equals(UserRole.ADMIN)) {
      List<UserResponse> page =
          userService.findAll(
              PageRequest.of(
                  pageable.getPageNumber(),
                  pageable.getPageSize(),
                  pageable.getSortOr(Sort.by(direction, sortBy))));
      return ResponseEntity.ok(page);
    } else if (role.equals(UserRole.USER)) {
      return ResponseEntity.ok(
          userService.findAllByRole(
              UserRole.CUSTOMER,
              PageRequest.of(
                  pageable.getPageNumber(),
                  pageable.getPageSize(),
                  pageable.getSortOr(Sort.by(direction, sortBy)))));
    }
    return ResponseEntity.ok(List.of());
  }

  private static @NotNull ResponseEntity<UserCreateResponse>
      getBadRequestUserCreateResponseResponseEntity(DataAccessException ex) {
    UserCreateResponse response = new UserCreateResponse(400, ex.getMessage(), false, null);
    return ResponseEntity.badRequest().body(response);
  }

  private static @NotNull ResponseEntity<UserCreateResponse>
      getUserAlreadyexistsCreateResponseResponseEntity() {
    UserCreateResponse response = new UserCreateResponse(409, "User already created!", false, null);
    return ResponseEntity.badRequest().body(response);
  }
}
