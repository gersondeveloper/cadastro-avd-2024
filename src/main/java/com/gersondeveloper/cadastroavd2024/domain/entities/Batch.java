package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity mapped to table "batch" according to the provided DDL.
 *
 * <p>register table batch ( id bigserial primary key, product_variant_id bigint not null references
 * product(id), variante_id bigint references product_variant(id), batch_code varchar(60),
 * valid_until date, local_id bigint not null, uom_base_balance numeric(14,4) not null default 0 );
 */
@Entity
@Table(name = "batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Batch extends BaseEntity {

  // Note: The DDL names are used literally for column names.
  // product_variant_id references product(id) in the DDL; we map it to Product here.
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_variant_id", nullable = false)
  private Product product;

  // variante_id references product_variant(id)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variante_id")
  private ProductVariant variant;

  @Column(name = "batch_code", length = 60)
  private String batchCode;

  @Column(name = "valid_until")
  private LocalDate validUntil;

  @Column(name = "local_id", nullable = false)
  private Long localId;

  @Column(name = "uom_base_balance", nullable = false, precision = 14, scale = 4)
  private BigDecimal uomBaseBalance = BigDecimal.ZERO;
}
