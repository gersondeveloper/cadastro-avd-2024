package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserFirstLoginRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserLoginRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserAuthenticationResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping({"/api/auth"})
@CrossOrigin(value = {"http://localhost:4200","http://localhost:8080"})
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Observed(name = "auth.login")
    @PostMapping(value = "/login", version = "v1")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(usernamePassword);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        var userDetails = (UserDetails) auth.getPrincipal();

        var token = tokenService.generateToken((User) Objects.requireNonNull(auth.getPrincipal()));
        log.info("Authentication controller was called by login");
        return ResponseEntity.ok(new UserAuthenticationResponse(userDetails, token));
    }

    @Observed(name = "auth.first-access")
    @PutMapping(value = "/first-access", version = "v1")
    public ResponseEntity<?> firstAccess(@RequestBody @Valid UserFirstLoginRequest data) {

        var user = (User) userService.findByEmail(data.email());
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (user.isEnabled()) {
            assert user.getPassword() != null;
            if (!user.getPassword().equals("change_the_password")) {
                return ResponseEntity.status(400).body("User already active");
            }
        }
        userService.changePassword(user, data.password());
        userService.setUserActive(user, true);
        userService.save(user);
        return ResponseEntity.ok().build();
    }
}
