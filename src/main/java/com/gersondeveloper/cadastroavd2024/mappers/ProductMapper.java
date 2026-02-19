package com.gersondeveloper.cadastroavd2024.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.gersondeveloper.cadastroavd2024.domain.dtos.Product;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.CategoryRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

  @Autowired private CategoryRepository categoryRepository;

  @Mapping(source = "uomBase", target = "baseUnitOfMeasurement")
  @Mapping(source = "uomBuy", target = "buyUnitOfMeasurement")
  public abstract Product toProduct(
      com.gersondeveloper.cadastroavd2024.domain.entities.Product product);

  @Mapping(source = "uomBase", target = "umBase")
  public abstract ProductResponse toProductResponse(
      com.gersondeveloper.cadastroavd2024.domain.entities.Product product);

  public abstract List<ProductResponse> toProductResponseList(
      List<com.gersondeveloper.cadastroavd2024.domain.entities.Product> products);

  @Mapping(source = "baseUnitOfMeasurement", target = "uomBase")
  @Mapping(source = "buyUnitOfMeasurement", target = "uomBuy")
  @Mapping(source = "categoryId", target = "category")
  public abstract com.gersondeveloper.cadastroavd2024.domain.entities.Product toProduct(
      CreateProductRequest request);

  public Category map(Long id) {
    if (id == null) return null;
    return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
  }
}
