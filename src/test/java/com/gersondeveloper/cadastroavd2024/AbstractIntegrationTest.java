package com.gersondeveloper.cadastroavd2024;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

public abstract class AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // Enable Testcontainers only when profile/property indicates it
        // Triggers if any of these are set: -Dtc=true, SPRING_PROFILES_ACTIVE contains "tc", or -Dspring.profiles.active contains "tc"
        boolean tcEnabled = Boolean.getBoolean("tc");
        String spa = System.getProperty("spring.profiles.active", "");
        String envSpa = System.getenv("SPRING_PROFILES_ACTIVE");
        if (spa != null && spa.contains("tc")) tcEnabled = true;
        if (envSpa != null && envSpa.contains("tc")) tcEnabled = true;

        if (tcEnabled) {
            // Use Testcontainers JDBC URL (requires Docker)
            registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:16-alpine:///testdb");
            registry.add("spring.datasource.username", () -> "test");
            registry.add("spring.datasource.password", () -> "test");
            registry.add("spring.datasource.driver-class-name", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");
            // Pooling for test speed and reliability
            registry.add("spring.datasource.hikari.maximum-pool-size", () -> "2");
            registry.add("spring.datasource.hikari.minimum-idle", () -> "1");
            registry.add("spring.datasource.hikari.auto-commit", () -> "true");
            // Let Liquibase run in TC profile (default false in test profile)
            registry.add("spring.liquibase.enabled", () -> true);
        }
    }

    String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
