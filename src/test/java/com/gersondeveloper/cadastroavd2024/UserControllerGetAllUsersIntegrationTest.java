package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;
import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UserRole;
import com.gersondeveloper.cadastroavd2024.infra.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(ObjectMapperTestConfig.class)
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class UserControllerGetAllUsersIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/user/all?role=ADMIN returns all users")
    void getAllUsers_asAdmin_returnsAllUsers() throws Exception {
        var u1 = new UserResponseDto(1L, "Alice", "alice@test.com", "Alice C.", "555-1111", UserRole.ADMIN, LocalDateTime.now(), true);
        var u2 = new UserResponseDto(2L, "Bob", "bob@test.com", "Bob B.", "555-2222", UserRole.USER, LocalDateTime.now(), true);
        Mockito.when(userService.findAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(
                        get("/api/user/all").param("role", UserRole.ADMIN.name())
                                .header("Api-Version", "v1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/user/all?role=USER returns only customers")
    void getAllUsers_asUser_returnsCustomers() throws Exception {
        var c1 = new UserResponseDto(3L, "Carol", "carol@test.com", "Carol C.", "555-3333", UserRole.CUSTOMER, LocalDateTime.now(), true);
        Mockito.when(userService.findAllByRole(UserRole.CUSTOMER)).thenReturn(List.of(c1));

        mockMvc.perform(
                        get("/api/user/all").param("role", UserRole.USER.name())
                                .header("Api-Version", "v1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/user/all?role=CUSTOMER returns empty list")
    void getAllUsers_asCustomer_returnsEmpty() throws Exception {
        mockMvc.perform(
                        get("/api/user/all").param("role", UserRole.CUSTOMER.name())
                                .header("Api-Version", "v1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
