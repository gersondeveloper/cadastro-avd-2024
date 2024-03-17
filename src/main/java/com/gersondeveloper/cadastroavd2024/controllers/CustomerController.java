package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.CustomerDto;
import com.gersondeveloper.cadastroavd2024.interfaces.CustomerRepository;
import com.gersondeveloper.cadastroavd2024.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")

public class CustomerController {


    private final CustomerService service;
    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerService service, CustomerRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomer() {
        var result = repository.findAll();
        var resultMapped = service.getAllCustomers(result);
        return new ResponseEntity<>(resultMapped, HttpStatus.OK);
    }

}
