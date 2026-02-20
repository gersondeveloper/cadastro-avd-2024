package com.gersondeveloper.cadastroavd2024.mappers;

import org.mapstruct.Mapper;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CategoryRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.CategoryCreateResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryCreateResponse toCategoryCreateResponse(Category category);

  Category toCategory(CategoryRegisterRequest request);
}
