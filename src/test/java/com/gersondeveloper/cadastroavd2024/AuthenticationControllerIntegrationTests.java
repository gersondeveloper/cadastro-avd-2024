package com.gersondeveloper.cadastroavd2024;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gersondeveloper.cadastroavd2024.domain.dtos.request.UserRegisterRequestDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testRegisterEndpoint() throws Exception {
        // Prepare request body
        UserRegisterRequestDto registerRequest = new UserRegisterRequestDto("teste@teste.com", "password", UserRole.ADMIN);
        String requestBody = objectMapper.writeValueAsString(registerRequest);

        // Perform POST request to /register endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Add more assertions as needed
    }

}
