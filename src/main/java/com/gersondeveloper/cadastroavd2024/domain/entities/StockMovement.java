package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.LocalType;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.MovementType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity mapped to table "stock_movement" according to the provided DDL.
 *
 * <p>register table stock_movement ( id bigserial primary key, movement_date timestamp not null
 * default now(), movement_type varchar(30) not null, // sera um enum do tipo MovementType
 * product_id bigint not null references product(id), product_variant_id bigint references
 * product_variant(id), batch_id bigint references batch(id), service_order_item_id bigint,
 * base_quantity numeric(14,4) not null, local_type varchar(50), -- sera um tipo LocalType
 * observation text );
 */
@Entity
@Table(name = "stock_movement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StockMovement extends BaseEntity {

  @Column(name = "movement_date", nullable = false)
  private LocalDateTime movementDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "movement_type", nullable = false, length = 30)
  private MovementType movementType;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_variant_id")
  private ProductVariant productVariant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "batch_id")
  private Batch batch;

  @Column(name = "service_order_item_id")
  private Long serviceOrderItemId;

  @Column(name = "base_quantity", nullable = false, precision = 14, scale = 4)
  private BigDecimal baseQuantity;

  @Enumerated(EnumType.STRING)
  @Column(name = "local_type", length = 50)
  private LocalType localType;

  @Column(name = "observation", columnDefinition = "text")
  private String observation;

  @PrePersist
  protected void onPrePersist() {
    if (this.movementDate == null) {
      this.movementDate = LocalDateTime.now();
    }
  }
}
