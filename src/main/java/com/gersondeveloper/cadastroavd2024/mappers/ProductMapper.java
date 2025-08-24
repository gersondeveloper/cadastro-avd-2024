package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.ProductDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto requestDto);
    Product toProduct(CreateProductRequestDto requestDto);
}
