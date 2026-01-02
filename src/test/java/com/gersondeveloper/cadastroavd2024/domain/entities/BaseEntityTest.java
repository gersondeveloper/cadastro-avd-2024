package com.gersondeveloper.cadastroavd2024.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseEntityTest {

    @Test
    void shouldHaveIsActiveTrueByDefault() {
        BaseEntity entity = new BaseEntity();
        assertTrue(entity.isActive(), "isActive should be true by default");
    }

    @Test
    void userShouldHaveIsActiveTrueByDefault() {
        User user = new User();
        assertTrue(user.isActive(), "User isActive should be true by default");
    }

    @Test
    void productShouldHaveIsActiveTrueByDefault() {
        Product product = new Product();
        assertTrue(product.isActive(), "Product isActive should be true by default");
    }
}
