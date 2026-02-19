package com.gersondeveloper.cadastroavd2024;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;

@SpringBootTest
@Import(ObjectMapperTestConfig.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test", "tc"})
public class CategoryControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldCreateNewCategory_andReturn201_onCategoryController() throws Exception {

    ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
    payload.put("name", "Eletrônicos");
    payload.put("description", "Categoria de produtos eletrônicos");
    payload.put("active", true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/category/register")
                .with(csrf())
                .header("Api-Version", "v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(payload)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("register")));
  }
}
