package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.math.BigDecimal;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity mapped to table "service_order_item" according to the provided DDL.
 *
 * <p>register table service_order_item ( id bigserial primary key, service_order_id bigint not null
 * references service_order(id), product_id bigint not null references product(id),
 * product_variant_id bigint references product_variante(id), uom_base varchar(5) not null, -- tipo
 * UomBase enum width_mm int, height_mm int, quantity numeric(12,3) not null, finishing
 * varchar(120), observation text, lost_factor numeric(5,2) default 0, base_expected_consumption
 * numeric(14,4) -- calculado );
 */
@Entity
@Table(name = "service_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceOrderItem extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_id", nullable = false)
  private ServiceOrder serviceOrder;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_variant_id")
  private ProductVariant productVariant;

  @Enumerated(EnumType.STRING)
  @Column(name = "uom_base", length = 5, nullable = false)
  private UomBase uomBase;

  @Column(name = "width_mm")
  private Integer widthMm;

  @Column(name = "height_mm")
  private Integer heightMm;

  @Column(name = "quantity", precision = 12, scale = 3, nullable = false)
  private BigDecimal quantity;

  @Column(name = "finishing", length = 120)
  private String finishing;

  @Column(name = "observation", columnDefinition = "text")
  private String observation;

  @Column(name = "lost_factor", precision = 5, scale = 2)
  private BigDecimal lostFactor = BigDecimal.ZERO;

  @Column(name = "base_expected_consumption", precision = 14, scale = 4)
  private BigDecimal baseExpectedConsumption;
}
