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
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@org.springframework.test.context.ActiveProfiles("dev")
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

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("name", "First User");
        payload.put("password", "Sup3rS3cret!");
        payload.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated());

        Map<String, Object> duplicate = new HashMap<>(payload);
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
