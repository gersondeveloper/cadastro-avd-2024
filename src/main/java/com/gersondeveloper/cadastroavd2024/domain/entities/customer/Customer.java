package com.gersondeveloper.cadastroavd2024.domain.entities.customer;

import com.gersondeveloper.cadastroavd2024.domain.dtos.request.CustomerRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Table(name = "customers")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Customer extends BaseEntity {

    @NotBlank
    @Column(unique = true)
    private String name;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String phone;
    private boolean isActive = true;


    public Customer (CustomerRequestDto customerRequestDto){
        this.name = customerRequestDto.name();
        this.email = customerRequestDto.email();
        this.phone = customerRequestDto.phone();
        this.creationDate = convertToDateViaInstant(customerRequestDto.creationDate());
        this.createdBy = customerRequestDto.createdBy();
        this.modificationDate = convertToDateViaInstant(customerRequestDto.modificationDate());
        this.modifiedBy = customerRequestDto.modifiedBy();
    }

    private static LocalDateTime convertToDateViaInstant (Date dateToConvert) {
            return dateToConvert != null ? dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime() : null;
    }
}
