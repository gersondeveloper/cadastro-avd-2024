package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequestDto;
import com.gersondeveloper.cadastroavd2024.infra.services.ProductService;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(value = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    private ProductMapper mapper;

    @Observed(name = "product.create")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(version = "v1")
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequestDto request, UriComponentsBuilder ucb) {
        if(request == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição inválida");

        var newProduct = mapper.toProduct(request);
        service.createProduct(newProduct);
        URI locationOfNewProduct = ucb
                .path("/api/product/{id}")
                .buildAndExpand(newProduct.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @Observed(name = "product.getAll")
    @SecurityRequirement(name="bearerAuth")
    @GetMapping(path = "/all", version = "v1")
    public ResponseEntity<?> getAll() {
        var products = service.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Observed(name = "product.getById")
    @SecurityRequirement(name="bearerAuth")
    @GetMapping (path = "/{id}", version = "v1")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        var product = service.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
