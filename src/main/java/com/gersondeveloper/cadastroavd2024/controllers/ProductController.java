package com.gersondeveloper.cadastroavd2024.controllers;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.ProductRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductRegisterResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.ProductService;
import com.gersondeveloper.cadastroavd2024.mappers.ProductMapper;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/product")
@CrossOrigin(value = "http://localhost:4200")
public class ProductController {

  private final ProductService productService;
  private final ProductMapper mapper;

  public ProductController(ProductService productService, ProductMapper mapper) {
    this.productService = productService;
    this.mapper = mapper;
  }

  @Observed(name = "product.register")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping(path = "/register", version = "v1")
  public ResponseEntity<CreateResponse<ProductRegisterResponse>> register(
      @RequestBody @Valid ProductRegisterRequest request, UriComponentsBuilder ucb) {
    if (request == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new CreateResponse<>(401, "Invalid request", false, null, null));

    Product newProduct;
    try {
      newProduct = productService.createProduct(mapper.toProduct(request));
    } catch (Exception e) {
      log.error("Error creating product: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new CreateResponse<>(
                  500,
                  "An error occurred while creating the product: " + e.getMessage(),
                  false,
                  null,
                  null));
    }
    String url = MessageFormat.format("api/product/register/{0}", newProduct.getId());
    CreateResponse<ProductRegisterResponse> response =
        new CreateResponse<>(
            201, "Product created successfully!", true, url, mapper.toProductResponse(newProduct));
    URI locationOfNewProduct =
        ucb.path("api/product/register/{id}").buildAndExpand(newProduct.getId()).toUri();
    log.info("Product created with id: {}", newProduct.getId());
    return ResponseEntity.created(locationOfNewProduct).body(response);
  }

  @Observed(name = "product.getAll")
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping(path = "/all", version = "v1")
  public ResponseEntity<CreateResponse<List<ProductResponse>>> getAll(
      @RequestParam UserRole role,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction,
      Pageable pageable) {

    if (role.equals(UserRole.ADMIN)) {
      List<ProductResponse> products =
          productService.findAll(
              PageRequest.of(
                  pageable.getPageNumber(),
                  pageable.getPageSize(),
                  pageable.getSortOr(Sort.by(direction, sortBy))));

      CreateResponse<List<ProductResponse>> response =
          new CreateResponse<>(200, "Products retrieved successfully!", true, null, products);

      return ResponseEntity.ok(response);
    } else {
      CreateResponse<List<ProductResponse>> response =
          new CreateResponse<>(
              403, "Only ADMIN users can see list of products.", false, null, null);

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
  }

  @Observed(name = "product.getById")
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping(path = "/{id}", version = "v1")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    var product = productService.getProductById(id);
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }
}
