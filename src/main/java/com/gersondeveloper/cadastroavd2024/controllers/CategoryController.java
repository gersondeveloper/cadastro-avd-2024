package com.gersondeveloper.cadastroavd2024.controllers;

import java.net.URI;
import java.text.MessageFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CategoryRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;
import com.gersondeveloper.cadastroavd2024.infra.services.CategoryService;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/category")
@CrossOrigin(value = "http://localhost:4200")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Observed(name = "product.category.create")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping(path = "/register", version = "v1")
  public ResponseEntity<?> register(
      @RequestBody @Valid CategoryRegisterRequest data, UriComponentsBuilder ucb) {

    Category newCategory;

    try {
      newCategory = this.categoryService.createCategory(data);
      String url = MessageFormat.format("/api/category/{0}", newCategory.getId());
      CreateResponse response = new CreateResponse(201, "User created successfully!", true, url);
      URI locationOfNewUser =
          ucb.path("/register/{id}").buildAndExpand(newCategory.getId()).toUri();
      log.info("Category '{}' created successfully", data.name());
      return ResponseEntity.created(locationOfNewUser).body(response);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
