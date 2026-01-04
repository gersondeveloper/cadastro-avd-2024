package com.gersondeveloper.cadastroavd2024.domain.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected LocalDateTime creationDate;
    //TODO: change after implement security
    protected String createdBy;
    protected LocalDateTime modificationDate;
    protected String modifiedBy;
    private boolean isActive;
}
