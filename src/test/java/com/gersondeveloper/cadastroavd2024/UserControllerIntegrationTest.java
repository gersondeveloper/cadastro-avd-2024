package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(ObjectMapperTestConfig.class)
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldRegisterUserAndReturn201_onUserController() throws Exception {
        ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("email", "user.controller@test.com");
        payload.put("name", "Controller User");
        payload.put("password", "Secret123!");
        payload.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/register/")));
    }

    @Test
    void shouldNotAllowDuplicateEmail_onUserController() throws Exception {
        String email = "duplicate.user@test.com";

        ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("email", email);
        payload.put("name", "First User");
        payload.put("password", "Sup3rS3cret!");
        payload.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated());

        ConcurrentHashMap<String, Object> duplicate = new ConcurrentHashMap<>(payload);
        duplicate.put("name", "Second User");

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(duplicate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.status").value(409));
    }
}
