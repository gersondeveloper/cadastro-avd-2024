package com.gersondeveloper.cadastroavd2024.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected LocalDateTime creationDate;
    //TODO: change after implement security
    protected String createdBy;
    protected LocalDateTime modificationDate;
    protected String modifiedBy;
}
