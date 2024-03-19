package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CustomerResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Customer;
import com.gersondeveloper.cadastroavd2024.interfaces.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        var customersList = repository
                .findAll()
                .stream()
                .map(CustomerResponseDto::new).toList();
        return new ResponseEntity<>(customersList, HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequestDto customerRequestDto){
        var customerToCreate = new Customer(customerRequestDto);
        var result = repository.save(customerToCreate);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
