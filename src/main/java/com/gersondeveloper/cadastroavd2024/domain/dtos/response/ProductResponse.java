package com.gersondeveloper.cadastroavd2024.domain.dtos.response;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;

public record ProductResponse(
    long id,
    String name,
    String description,
    UomBase umBase,
    UomBuy uomBuy,
    Double conversionBaseToBuy,
    int currentStock,
    int minStock,
    int maxStock,
    int stockAlert) {}
