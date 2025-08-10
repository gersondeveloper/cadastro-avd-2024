package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserAuthenticationRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserAuthenticationResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.user.User;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserAuthenticationRequestDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(usernamePassword);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        var userDetails = (UserDetails) auth.getPrincipal();
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new UserAuthenticationResponseDto(userDetails, token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody @Valid UserRegisterRequestDto data) throws URISyntaxException {

        UserCreateResponse response = new UserCreateResponse();

        if (this.userRepository.findByLogin(data.login()) != null) {
            return getUserAlreadyexistsCreateResponseResponseEntity(response);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), data.name(), encryptedPassword, data.role());
        try {
            this.userRepository.save(newUser);
        } catch (DataAccessException ex) {
            return getBadRequestUserCreateResponseResponseEntity(ex, response);
        }
        response.setStatus(201);
        response.setStatusText("Usuário criado com sucesso!");
        response.setOk(true);
        response.setUrl("/register/" + newUser.getId());
        return ResponseEntity.created(new URI(response.getUrl())).body(response);
    }

    private static @NotNull ResponseEntity<UserCreateResponse> getBadRequestUserCreateResponseResponseEntity(DataAccessException ex, UserCreateResponse response) {
        response.setStatus(400);
        response.setOk(false);
        response.setOk(false);
        response.setStatusText(ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private static @NotNull ResponseEntity<UserCreateResponse> getUserAlreadyexistsCreateResponseResponseEntity(UserCreateResponse response) {
        response.setStatus(409);
        response.setStatusText("Usuário já existe");
        response.setOk(false);
        return ResponseEntity.badRequest().body(response);
    }

}
