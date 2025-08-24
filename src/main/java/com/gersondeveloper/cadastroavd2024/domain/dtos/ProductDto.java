package com.gersondeveloper.cadastroavd2024.domain.dtos;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;

public record ProductDto(String name, String description, UomBase baseUnitOfMeasurement,
                         UomBuy buyUnitOfMeasurement, Double conversionBaseToBuy) {
}
