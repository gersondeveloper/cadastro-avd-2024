package com.gersondeveloper.cadastroavd2024;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(ObjectMapperTestConfig.class)
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
public class AuthenticationFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterThenFirstAccessThenLogin() throws Exception {
        // Unique email to avoid conflicts across runs
        String email = "auth.flow.user+" + System.currentTimeMillis() + "@test.com";
        String initialPassword = "change_the_password"; // placeholder used by first access flow
        String newPassword = "Sup3rStrongPass!";

        // 1) Register
        ConcurrentHashMap<String, Object> register = new ConcurrentHashMap<>();
        register.put("email", email);
        register.put("name", "Auth Flow User");
        register.put("password", initialPassword);
        register.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .header("Api-Version", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        // 2) First access - set a real password and activate account
        ConcurrentHashMap<String, Object> firstAccess = new ConcurrentHashMap<>();
        firstAccess.put("email", email);
        firstAccess.put("password", newPassword);

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .header("Api-Version", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isOk());

        // 3) Login with the new password
        ConcurrentHashMap<String, Object> login = new ConcurrentHashMap<>();
        login.put("email", email);
        login.put("password", newPassword);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .header("Api-Version", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", Matchers.not(Matchers.nullValue())))
                .andExpect(jsonPath("$.userDetails.username").value(email));
    }

    protected String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

}
