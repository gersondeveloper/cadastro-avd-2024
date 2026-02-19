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

  @JoinColumn(name = "category_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  private UomBase uomBase; // Unit of Measure Base

  @Enumerated(EnumType.STRING)
  private UomBuy uomBuy; // Unit of Measure Base for Buy

  @Column(name = "conversion_base_to_buy", precision = 12) // width of roll
  private Double conversionBaseToBuy; // Conversion factor for UOM

  @Column(name = "current_stock", nullable = false)
  private int currentStock = 0; // quantity rolls in stock

  @Column(name = "min_stock", nullable = false)
  private int minStock = 1; // min quantity in stock

  @Column(name = "max_stock")
  private int maxStock = 5; // max quantity in stock

  @Column(name = "stock_alert", nullable = false)
  private int stockAlert = 1; // alert for minimum stock
}
