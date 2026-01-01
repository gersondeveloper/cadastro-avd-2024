package com.gersondeveloper.cadastroavd2024.domain.dtos.request;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;

public record CreateProductRequestDto(String name, String description, UomBase baseUnitOfMeasurement,
                                      UomBuy buyUnitOfMeasurement, Double conversionBaseToBuy, int minStock, int maxStock, int stockAlert) {
}
