package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequestDto;
import com.gersondeveloper.cadastroavd2024.infra.services.ProductService;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin(value = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    private ProductMapper mapper;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequestDto request) {
        if(request == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição inválida");

        var newProduct = mapper.toProduct(request);
        service.createProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body("Produto criado com sucesso");
    }

    @SecurityRequirement(name="bearerAuth")
    @GetMapping
    public ResponseEntity<?> getAll() {
        var products = service.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @SecurityRequirement(name="bearerAuth")
    @GetMapping ("/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        var product = service.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
