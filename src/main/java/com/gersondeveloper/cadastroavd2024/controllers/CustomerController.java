package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CustomerResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Customer;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;
import com.gersondeveloper.cadastroavd2024.interfaces.CustomerRepository;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")

public class CustomerController {

    private final CustomerRepository repository;

    @Autowired

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        var customersList = repository
                .findAll()
                .stream()
                .map(CustomerResponseDto::new).toList();
        return new ResponseEntity<>(customersList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable long id){

        var customer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        return ResponseEntity.ok(new CustomerResponseDto(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) throws BadRequestException {

        repository.save(new Customer(customerRequestDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
