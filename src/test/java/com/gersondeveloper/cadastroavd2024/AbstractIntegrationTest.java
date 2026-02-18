package com.gersondeveloper.cadastroavd2024;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractIntegrationTest {

  @Autowired ObjectMapper objectMapper;

  @Autowired protected WebApplicationContext context;

  protected MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    boolean tcEnabled = Boolean.getBoolean("tc");
    String spa = System.getProperty("spring.profiles.active", "");
    String envSpa = System.getenv("SPRING_PROFILES_ACTIVE");
    if (spa != null && spa.contains("tc")) tcEnabled = true;
    if (envSpa != null && envSpa.contains("tc")) tcEnabled = true;

    if (tcEnabled) {
      registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:16-alpine:///testdb");
      registry.add("spring.datasource.username", () -> "test");
      registry.add("spring.datasource.password", () -> "test");
      registry.add(
          "spring.datasource.driver-class-name",
          () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");
      registry.add("spring.datasource.hikari.maximum-pool-size", () -> "2");
      registry.add("spring.datasource.hikari.minimum-idle", () -> "1");
      registry.add("spring.datasource.hikari.auto-commit", () -> "true");
      registry.add("spring.liquibase.enabled", () -> true);
    }
  }

  String toJson(Object obj) throws Exception {
    return objectMapper.writeValueAsString(obj);
  }
}
