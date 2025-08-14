package com.gersondeveloper.cadastroavd2024.domain.entities.serviceOrder;

import com.gersondeveloper.cadastroavd2024.domain.entities.BaseEntity;
import com.gersondeveloper.cadastroavd2024.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity mapped to table "service_order" according to the provided DDL.
 *
 * create table service_order (
 *   id bigserial primary key,
 *   code varchar(20) unique,
 *   create_date date not null,
 *   deadline date,
 *   upfront_payment_value numeric(12,2),
 *   balance numeric(12,2),
 *   contact varchar(120),
 *   instalation_date date,
 *   intalation_time time,
 *   instalation_location varchar(255),
 *   observation text,
 *   service_description text,
 *   status varchar(20) not null default 'DRAFT',
 *   user_id bigint not null references user(id)
 * );
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

    @Column(name = "instalation_date")
    private LocalDate instalationDate;

    // Note: the column name has a typo in the DDL ("intalation_time" without 's'). We map it literally.
    @Column(name = "intalation_time")
    private LocalTime intalationTime;

    @Column(name = "instalation_location", length = 255)
    private String instalationLocation;

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
