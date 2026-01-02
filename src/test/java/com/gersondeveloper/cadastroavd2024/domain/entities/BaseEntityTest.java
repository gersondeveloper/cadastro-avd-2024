package com.gersondeveloper.cadastroavd2024.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseEntityTest {

    @Test
    void shouldHaveIsActiveTrueByDefault() {
        BaseEntity entity = new BaseEntity();
        assertFalse(entity.isActive(), "isActive should be false by default");
    }

    @Test
    void userShouldHaveIsActiveTrueByDefault() {
        User user = new User();
        assertFalse(user.isActive(), "User isActive should be false by default");
    }

    @Test
    void productShouldHaveIsActiveTrueByDefault() {
        Product product = new Product();
        assertFalse(product.isActive(), "Product isActive should be false by default");
    }
}
