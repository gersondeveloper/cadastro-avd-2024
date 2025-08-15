package com.gersondeveloper.cadastroavd2024.domain.entities;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Column(name = "description", length = 240, nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private UomBase uomBase; // Unit of Measure Base

    @Enumerated(EnumType.ORDINAL)
    private UomBase uomBuy; // Unit of Measure Base for Buy

    @Column(name = "conversion_base_to_buy", precision = 12)
    private Double conversionBaseToBuy; // Conversion factor for UOM

}
