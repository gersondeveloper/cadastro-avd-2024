package com.gersondeveloper.cadastroavd2024.domain.entities;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Column(name = "description", length = 240, nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private UomBase uomBase; // Unit of Measure Base

    @Enumerated(EnumType.ORDINAL)
    private UomBuy uomBuy; // Unit of Measure Base for Buy

    @Column(name = "conversion_base_to_buy", precision = 12)
    private Double conversionBaseToBuy; // Conversion factor for UOM

}
