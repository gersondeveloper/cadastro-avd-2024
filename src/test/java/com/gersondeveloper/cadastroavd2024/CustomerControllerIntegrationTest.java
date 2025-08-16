package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateANewCustomerAndReturn200WithAuth() throws Exception {
        // 1) Register a user for authentication
        String email = "test.user.customer@test.com";
        String password = "Secret123!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "Test User");
        register.put("password", password);
        register.put("role", UserRole.ADMIN.name());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        // 2) Login and get JWT token
        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        // 3) Prepare customer payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "johnCustomer@teste.com");
        payload.put("name", "John Doe The Customer");
        payload.put("password", "Teste123");
        payload.put("role", UserRole.CUSTOMER.name());
        payload.put("phone", "913995497");
        payload.put("contactName", "Gerson Filho");

        // 4) Call protected endpoint with Authorization header
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(toJson(payload)))
                .andExpect(status().isOk());
    }
}
