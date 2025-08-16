package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserLoginRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserAuthenticationResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.interfaces.UserRepository;
import com.gersondeveloper.cadastroavd2024.services.TokenService;
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
@RequestMapping("api/auth")
@CrossOrigin(value = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

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

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody @Valid UserRegisterRequestDto data) throws URISyntaxException {

        if (this.userRepository.findByEmail(data.email()) != null) {
            return getUserAlreadyexistsCreateResponseResponseEntity();
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.email(), data.name(), encryptedPassword, data.role());
        try {
            this.userRepository.save(newUser);
        } catch (DataAccessException ex) {
            return getBadRequestUserCreateResponseResponseEntity(ex);
        }
        String url = MessageFormat.format("/register/{0}", newUser.getId());
        UserCreateResponse response = new UserCreateResponse(201, "User created successfully!", true, url);
        return ResponseEntity.created(new URI(url)).body(response);
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
