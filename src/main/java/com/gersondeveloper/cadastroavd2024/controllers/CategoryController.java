package com.gersondeveloper.cadastroavd2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateCategoryRequest;
import com.gersondeveloper.cadastroavd2024.infra.services.CategoryService;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/product/category")
@CrossOrigin(value = "http://localhost:4200")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @Observed(name = "product.category.create")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping(version = "v1")
  public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
    return null;
  }
}
