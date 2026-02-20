package com.gersondeveloper.cadastroavd2024.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.ProductRegisterRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductRegisterResponse;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.CategoryRepository;
import com.gersondeveloper.cadastroavd2024.exceptions.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

  @Autowired private CategoryRepository categoryRepository;

  @Mapping(source = "uomBase", target = "umBase")
  @Mapping(source = "active", target = "isActive")
  public abstract ProductRegisterResponse toProductResponse(Product product);

  public abstract List<ProductResponse> toProductResponseList(List<Product> products);

  @Mapping(source = "baseUnitOfMeasurement", target = "uomBase")
  @Mapping(source = "buyUnitOfMeasurement", target = "uomBuy")
  @Mapping(source = "categoryId", target = "category")
  @Mapping(source = "isActive", target = "active")
  public abstract Product toProduct(ProductRegisterRequest request);

  public Category map(Long id) {
    if (id == null) return null;
    return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
  }
}
