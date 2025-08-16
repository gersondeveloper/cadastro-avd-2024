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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerIntegrationTests extends AbstractIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldRegisterUserAndReturn201() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "john.doe@test.com");
        payload.put("name", "John Doe");
        payload.put("password", "Secret123!");
        payload.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/register/")));
    }

    @Test
    void shouldLoginWithRegisteredUserAndReceiveToken() throws Exception {
        String email = "jane.doe@test.com";
        String password = "Sup3rS3cret!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "Jane Doe");
        register.put("password", password);
        register.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
