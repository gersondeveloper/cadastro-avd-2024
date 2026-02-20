package com.gersondeveloper.cadastroavd2024.infra.services;

import org.springframework.stereotype.Service;

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
  public Category createCategory(Category category) {
    categoryRepository.save(category);
    log.info("Creating category '{}'", category.getName());
    return category;
  }
}
