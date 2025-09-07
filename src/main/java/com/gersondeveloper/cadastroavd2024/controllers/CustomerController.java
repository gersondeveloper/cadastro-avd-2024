package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateCustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import com.gersondeveloper.cadastroavd2024.mappers.CustomerMapper;
import com.gersondeveloper.cadastroavd2024.infra.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/customer", "/api/v1/customer"})
@CrossOrigin(value = "http://localhost:4200")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCustomerRequestDto request) {
        if(request != null) {
            var newUser = customerMapper.toUser(request);
            customerService.save(newUser);
            // Gera um token de confirmação (reutilizando o JWT atual)
            String confirmToken = tokenService.generateToken(newUser);
            // Envia e-mail com o token para confirmação
            emailService.sendTokenEmail(newUser.getEmail(), confirmToken);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição inválida");
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(customerService.findAllByRole(UserRole.CUSTOMER));
    }
}
