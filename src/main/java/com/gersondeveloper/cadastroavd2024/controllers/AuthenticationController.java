package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserFirstLoginRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserLoginRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserAuthenticationResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.infra.services.AuthorizationService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;
import com.gersondeveloper.cadastroavd2024.mappers.UserMapper;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;


@RestController
@RequestMapping({"/api/auth", "/api/v1/auth"})
@CrossOrigin(value = {"http://localhost:4200","http://localhost:8080"})
public class AuthenticationController {



    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequestDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(usernamePassword);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        var userDetails = (UserDetails) auth.getPrincipal();

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new UserAuthenticationResponseDto(userDetails, token));
    }

    @PutMapping("/first-access")
    public ResponseEntity<?> firstAccess(@RequestBody @Valid UserFirstLoginRequest data) {

        var user = (User) userService.findByEmail(data.email());
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (user.isActive() && !user.getPassword().equals("change_the_password")) {
            return ResponseEntity.status(400).body("User already active");
        }
        userService.changePassword(user, data.password());
        userService.setUserActive(user, true);
        userService.save(user);
        return ResponseEntity.ok().build();
    }
}
