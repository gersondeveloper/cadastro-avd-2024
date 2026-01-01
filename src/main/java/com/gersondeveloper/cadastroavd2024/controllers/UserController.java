package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;
import com.gersondeveloper.cadastroavd2024.mappers.UserMapper;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
@CrossOrigin(value = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    @Observed(name = "user.register")
    @PostMapping(path = "/register", version = "v1")
    public ResponseEntity<UserCreateResponse> register(@RequestBody @Valid UserRegisterRequestDto data, UriComponentsBuilder  ucb) {

        var newUser = new User();

        if (this.userService.findByEmail(data.email()) != null) {
            return getUserAlreadyexistsCreateResponseResponseEntity();
        }

        try {
            newUser = this.userService.registerNewUser(data);
        } catch (DataAccessException ex) {
            return getBadRequestUserCreateResponseResponseEntity(ex);
        }
        String confirmToken = tokenService.generateToken(newUser);
        emailService.sendConfirmationEmail(newUser, confirmToken);

        String url = MessageFormat.format("/register/{0}", newUser.getId());
        UserCreateResponse response = new UserCreateResponse(201, "User created successfully!", true, url);
        URI locationOfNewUser = ucb
                .path("/register/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).body(response);
    }

    @Observed(name = "user.getAll")
    @GetMapping(path = "/all", version = "v1")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam UserRole role,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "DESC") Sort.Direction direction,
                                                  Pageable pageable) {
        if(role.equals(UserRole.ADMIN)) {
            List<UserResponseDto> page = userService.findAll(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(direction, sortBy))));
            return ResponseEntity.ok(userMapper.toUserList(page));
        } else if (role.equals(UserRole.USER)) {
            return ResponseEntity.ok(userMapper.toUserList(userService.findAllByRole(UserRole.CUSTOMER)));
        }
        return ResponseEntity.ok(List.of());
    }

    private static @NotNull ResponseEntity<UserCreateResponse> getBadRequestUserCreateResponseResponseEntity(DataAccessException ex) {
        UserCreateResponse response = new UserCreateResponse(400, ex.getMessage(), false, null);
        return ResponseEntity.badRequest().body(response);
    }

    private static @NotNull ResponseEntity<UserCreateResponse> getUserAlreadyexistsCreateResponseResponseEntity() {
        UserCreateResponse response = new UserCreateResponse(409, "User already created!", false, null);
        return ResponseEntity.badRequest().body(response);
    }
}
