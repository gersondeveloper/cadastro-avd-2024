package com.gersondeveloper.cadastroavd2024.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Table(name = "customers")
@Entity
@Data
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull("Name is required")
    private String name;
    @NotNull("Email is required")
    private String email;
    @NotNull("Phone is required")
    private String phone;
    private boolean isActive = true;
    private LocalDateTime creationDate = LocalDateTime.now();
    //TODO: change after implement security
    private String createdBy = "Gerson C Filho";
    private LocalDateTime modificationDate;
    private String modifiedBy;
}
