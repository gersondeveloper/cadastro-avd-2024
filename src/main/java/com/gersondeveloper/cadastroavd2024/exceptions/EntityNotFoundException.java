package com.gersondeveloper.cadastroavd2024.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(long id) {
        super(String.format("User: %d was not found", id));
    }
}
