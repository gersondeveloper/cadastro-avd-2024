package com.gersondeveloper.cadastroavd2024.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(long id) {
        super(String.format("Customer: %d was not found", id));
    }
}
