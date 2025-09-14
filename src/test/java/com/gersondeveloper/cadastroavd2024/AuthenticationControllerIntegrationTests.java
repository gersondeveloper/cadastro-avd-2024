package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import org.hamcrest.Matchers;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@org.springframework.test.context.ActiveProfiles("dev")
public class AuthenticationControllerIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldAuthenticateWithValidCredentials_onAuthenticationController() throws Exception {
        String email = "auth.user@test.com";
        String password = "StrongPassw0rd!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "Auth User");
        register.put("password", password);
        register.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        Map<String, Object> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", Matchers.not(Matchers.isEmptyOrNullString())))
                .andExpect(jsonPath("$.userDetails.username").value(email));
    }

    @Test
    void shouldReturn401ForInvalidCredentials_onAuthenticationController() throws Exception {
        Map<String, Object> login = new HashMap<>();
        login.put("email", "not.exists@test.com");
        login.put("password", "WrongPass123!");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(Matchers.containsString("Invalid email or password")));
    }

    @Test
    void shouldActivateUserAndSetPassword_onFirstAccess() throws Exception {
        String email = "first.access.user@test.com";
        String initialPassword = "change_the_password";
        String newPassword = "NewStrongPassw0rd!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "First Access User");
        register.put("password", initialPassword);
        register.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        ConcurrentHashMap<String, Object> firstAccess = new ConcurrentHashMap<>();
        firstAccess.put("email", email);
        firstAccess.put("password", newPassword);

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("User already active")));
    }

    @Test
    void shouldReturn400WhenUserAlreadyActive_onFirstAccess() throws Exception {
        String email = "already.active.user@test.com";
        String initialPassword = "InitPassw0rd!";
        String newPassword = "OtherStrongPassw0rd!";

        Map<String, Object> register = new HashMap<>();
        register.put("email", email);
        register.put("name", "Already Active User");
        register.put("password", initialPassword);
        register.put("role", UserRole.USER.name());

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(register)))
                .andExpect(status().isCreated());

        Map<String, Object> firstAccess = new HashMap<>();
        firstAccess.put("email", email);
        firstAccess.put("password", newPassword);

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("User already active")));
    }

    @Test
    void shouldReturn404WhenUserNotFound_onFirstAccess() throws Exception {
        Map<String, Object> firstAccess = new HashMap<>();
        firstAccess.put("username", "notfound.user@test.com");
        firstAccess.put("password", "SomeStrongPass123!");

        mockMvc.perform(put("/api/auth/first-access")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(firstAccess)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("User not found")));
    }
}
