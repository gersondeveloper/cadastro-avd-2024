package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(ObjectMapperTestConfig.class)
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles({"test","tc"})
public class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateProduct_andReturn201_onProductController() throws Exception {
        ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("name", "Papel de Parede Modelo A");
        payload.put("description", "Papel de parede vinílico");
        payload.put("baseUnitOfMeasurement", UomBase.M2.name());
        payload.put("buyUnitOfMeasurement", UomBuy.ROLL.name());
        payload.put("conversionBaseToBuy", 5.0);

        mockMvc.perform(post("/api/v1/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Produto criado com sucesso"));
    }

    @Test
    void shouldGetAllProducts_andReturn200_onProductController() throws Exception {
        // Ensure at least one product exists
        ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("name", "Produto de Teste B");
        payload.put("description", "Descrição do Produto de Teste B");
        payload.put("baseUnitOfMeasurement", UomBase.UN.name());
        payload.put("buyUnitOfMeasurement", UomBuy.UN.name());
        payload.put("conversionBaseToBuy", 1.0);

        mockMvc.perform(post("/api/v1/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void shouldGetProductById_andReturn200_onProductController() throws Exception {
        // Create a product first
        ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("name", "Produto para Buscar");
        payload.put("description", "Descrição do Produto para Buscar");
        payload.put("baseUnitOfMeasurement", UomBase.UN.name());
        payload.put("buyUnitOfMeasurement", UomBuy.UN.name());
        payload.put("conversionBaseToBuy", 1.0);

        String location = mockMvc.perform(post("/api/v1/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(payload)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        // If controller does not set Location header, fallback to fetch all and get last id via JSON parsing
        Long id;
        if (location != null && location.matches(".*/(\\d+)$")) {
            id = Long.parseLong(location.replaceAll(".*\\/(\\d+)$", "$1"));
        } else {
            var result = mockMvc.perform(get("/api/v1/product")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            // Use ObjectMapper from AbstractIntegrationTest for robust parsing
            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode array = om.readTree(responseJson);
            com.fasterxml.jackson.databind.JsonNode last = array.get(array.size() - 1);
            id = last.get("id").asLong();
        }

        mockMvc.perform(get("/api/v1/product/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenBodyIsMissing_onProductController() throws Exception {
        mockMvc.perform(post("/api/v1/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
