package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(value = "http://localhost:4200")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    @Observed(name = "user.register")
    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody @Valid UserRegisterRequestDto data) throws URISyntaxException {

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
        return ResponseEntity.created(new URI(url)).body(response);
    }

    @Observed(name = "user.getAll")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam UserRole role) {
        if(role.equals(UserRole.ADMIN)) {
            return ResponseEntity.ok(userService.findAll());
        } else if (role.equals(UserRole.USER)) {
            return ResponseEntity.ok(userService.findAllByRole(UserRole.CUSTOMER));
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
