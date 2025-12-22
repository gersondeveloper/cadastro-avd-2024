package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.ProductDto;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CreateProductRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "uomBase", target = "baseUnitOfMeasurement")
    @Mapping(source = "uomBuy", target = "buyUnitOfMeasurement")
    ProductDto toProductDto(Product product);

    @Mapping(source = "baseUnitOfMeasurement", target = "uomBase")
    @Mapping(source = "buyUnitOfMeasurement", target = "uomBuy")
    Product toProduct(ProductDto requestDto);

    @Mapping(source = "baseUnitOfMeasurement", target = "uomBase")
    @Mapping(source = "buyUnitOfMeasurement", target = "uomBuy")
    Product toProduct(CreateProductRequestDto requestDto);
}
