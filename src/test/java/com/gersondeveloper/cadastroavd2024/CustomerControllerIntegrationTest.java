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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private String obtainToken(UserRole role) throws Exception {
        String email = "user_" + role.name().toLowerCase() + System.currentTimeMillis() + "@test.com";
        String password = "Secret123!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "Test User " + role.name());
        register.put("password", password);
        register.put("role", role.name());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        return objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();
    }

    private Map<String, Object> validCustomerPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "customer_" + System.currentTimeMillis() + "@test.com");
        payload.put("name", "John Customer");
        payload.put("password", "Teste123");
        payload.put("role", UserRole.CUSTOMER.name());
        payload.put("phone", "913995497");
        payload.put("contactName", "Contact Person");
        return payload;
    }

    // Happy path: admin creates a new customer
    @Test
    void shouldCreateANewCustomerAndReturn200WithAdminAuth() throws Exception {
        String adminToken = obtainToken(UserRole.ADMIN);

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(toJson(validCustomerPayload())))
                .andExpect(status().isOk());
    }

    // Edge: missing Authorization header
    @Test
    void shouldRejectCreateCustomerWithoutAuthorization() throws Exception {
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validCustomerPayload())))
                .andExpect(status().isForbidden());
    }

    // Edge: authenticated as CUSTOMER (not ADMIN)
    @Test
    void shouldForbidCreateCustomerWithCustomerRole() throws Exception {
        String customerToken = obtainToken(UserRole.CUSTOMER);

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + customerToken)
                        .content(toJson(validCustomerPayload())))
                .andExpect(status().isForbidden());
    }

    // Edge: admin but missing request body
    @Test
    void shouldReturnBadRequestWhenMissingRequestBody() throws Exception {
        String adminToken = obtainToken(UserRole.ADMIN);

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    // Happy: list customers with admin auth
    @Test
    void shouldListCustomersWithAdminAuth() throws Exception {
        String adminToken = obtainToken(UserRole.ADMIN);

        mockMvc.perform(get("/api/customer")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    // Edge: list customers without auth
    @Test
    void shouldRejectListCustomersWithoutAuthorization() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isForbidden());
    }
}
