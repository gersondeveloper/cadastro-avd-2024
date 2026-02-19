package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Category extends BaseEntity {

  private String name;
  private String description;
  private boolean active = true;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private List<Product> products;
}
