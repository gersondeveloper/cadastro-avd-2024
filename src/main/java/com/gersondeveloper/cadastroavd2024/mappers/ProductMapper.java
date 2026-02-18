package com.gersondeveloper.cadastroavd2024.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gersondeveloper.cadastroavd2024.domain.dtos.Product;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequest;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(source = "uomBase", target = "baseUnitOfMeasurement")
  @Mapping(source = "uomBuy", target = "buyUnitOfMeasurement")
  Product toProduct(com.gersondeveloper.cadastroavd2024.domain.entities.Product product);

  @Mapping(source = "uomBase", target = "umBase")
  ProductResponse toProductResponse(
      com.gersondeveloper.cadastroavd2024.domain.entities.Product product);

  List<ProductResponse> toProductResponseList(
      List<com.gersondeveloper.cadastroavd2024.domain.entities.Product> products);

  @Mapping(source = "baseUnitOfMeasurement", target = "uomBase")
  @Mapping(source = "buyUnitOfMeasurement", target = "uomBuy")
  com.gersondeveloper.cadastroavd2024.domain.entities.Product toProduct(
      CreateProductRequest request);
}
