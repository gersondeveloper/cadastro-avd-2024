package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateCustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.mappers.CustomerMapper;
import com.gersondeveloper.cadastroavd2024.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(value = "http://localhost:4200")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCustomerRequestDto request) {
        if(request != null) {
            var newUser = customerMapper.toUser(request);
            customerService.save(newUser);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
