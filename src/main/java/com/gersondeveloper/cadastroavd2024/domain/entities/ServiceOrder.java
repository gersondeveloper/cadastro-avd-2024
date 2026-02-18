package com.gersondeveloper.cadastroavd2024.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity mapped to table "service_order" according to the provided DDL.
 *
 * <p>create table service_order ( id bigserial primary key, code varchar(20) unique, create_date
 * date not null, deadline date, upfront_payment_value numeric(12,2), balance numeric(12,2), contact
 * varchar(120), installation_date date, installation_time time, installation_location varchar(255),
 * observation text, service_description text, status varchar(20) not null default 'DRAFT', user_id
 * bigint not null references user(id) );
 */
@Entity
@Table(name = "service_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceOrder extends BaseEntity {

  @Column(name = "code", length = 20, unique = true)
  private String code;

  @Column(name = "create_date", nullable = false)
  private LocalDate createDate;

  @Column(name = "deadline")
  private LocalDate deadline;

  @Column(name = "upfront_payment_value", precision = 12, scale = 2)
  private BigDecimal upfrontPaymentValue;

  @Column(name = "balance", precision = 12, scale = 2)
  private BigDecimal balance;

  @Column(name = "contact", length = 120)
  private String contact;

  @Column(name = "installation_date")
  private LocalDate installationDate;

  @Column(name = "installation_time")
  private LocalTime installationTime;

  @Column(name = "installation_location", length = 255)
  private String installationLocation;

  @Column(name = "observation", columnDefinition = "text")
  private String observation;

  @Column(name = "service_description", columnDefinition = "text")
  private String serviceDescription;

  @Column(name = "status", length = 20, nullable = false)
  private String status = "DRAFT";

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
