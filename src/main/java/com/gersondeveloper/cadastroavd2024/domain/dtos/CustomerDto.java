package com.gersondeveloper.cadastroavd2024.domain.dtos;

import java.time.LocalDateTime;

public class CustomerDto {
    private long id;
    private String name;
    private String email;
    private String phone;
    private boolean isActive;
    private LocalDateTime creationDate;
    private String createdBy;
    private LocalDateTime modificationDate;
    private String modifiedBy;
}
