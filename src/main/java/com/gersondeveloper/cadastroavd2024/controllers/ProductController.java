package com.gersondeveloper.cadastroavd2024.controllers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(value = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Observed(name = "product.create")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(version = "v1")
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequest request, UriComponentsBuilder ucb) {
        if(request == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição inválida");

        var newProduct = productService.createProduct(request);
        URI locationOfNewProduct = ucb
                .path("/api/product/{id}")
                .buildAndExpand(newProduct.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @Observed(name = "product.getAll")
    @SecurityRequirement(name="bearerAuth")
    @GetMapping(path = "/all", version = "v1")
    public ResponseEntity<?> getAll(
            @RequestParam UserRole role,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(defaultValue = "DESC")Sort.Direction direction,
            Pageable pageable) {

        if(role.equals(UserRole.ADMIN)) {
            List<ProductResponse> products = productService.findAll(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(direction, sortBy))));

            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN users can see list of products.");
        }
    }

    @Observed(name = "product.getById")
    @SecurityRequirement(name="bearerAuth")
    @GetMapping (path = "/{id}", version = "v1")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        var product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
