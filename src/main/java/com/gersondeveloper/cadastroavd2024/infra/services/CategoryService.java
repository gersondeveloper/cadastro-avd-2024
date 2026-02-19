package com.gersondeveloper.cadastroavd2024.infra.services;

import org.springframework.stereotype.Service;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CategoryRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.CategoryRepository;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Observed(name = "category.create")
  public Category createCategory(CategoryRegisterRequest request) {
    var category = new Category();
    category.setName(request.name());
    category.setDescription(request.description());
    category.setActive(request.active());
    log.info("Creating category '{}'", request.name());
    categoryRepository.save(category);
    return category;
  }
}
