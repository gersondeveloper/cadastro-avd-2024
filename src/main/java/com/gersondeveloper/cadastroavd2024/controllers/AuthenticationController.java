package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserAuthenticationResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.user.User;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserAuthenticationRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.services.TokenService;
import com.gersondeveloper.cadastroavd2024.interfaces.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity login(@RequestBody @Valid UserAuthenticationRequestDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var userDetails = (UserDetails) auth.getPrincipal();

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new UserAuthenticationResponseDto(userDetails, token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterRequestDto data) {

         if(this.userRepository.findByLogin(data.login()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), data.name(), encryptedPassword, data.role());
        try{
            this.userRepository.save(newUser);
        } catch (DataAccessException ex) {
            var message = ex.getMessage();
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok().build();
    }

}
