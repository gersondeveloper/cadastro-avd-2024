package com.gersondeveloper.cadastroavd2024.domain.entities.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA entity mapping to table "client" as described:
 * create table client (
 *   id bigserial primary key,
 *   name varchar(120) not null,
 *   phone varchar(40),
 *   email varchar(120)
 * );
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Column(name = "phone", length = 40)
    private String phone;

    @Column(name = "email", length = 120)
    private String email;
}
