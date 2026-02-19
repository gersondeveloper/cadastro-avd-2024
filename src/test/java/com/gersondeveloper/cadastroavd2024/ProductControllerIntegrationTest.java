package com.gersondeveloper.cadastroavd2024;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.gersondeveloper.cadastroavd2024.configuration.ObjectMapperTestConfig;
import com.gersondeveloper.cadastroavd2024.domain.entities.Category;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBase;
import com.gersondeveloper.cadastroavd2024.domain.entities.enums.UomBuy;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.CategoryRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(ObjectMapperTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test", "tc"})
public class ProductControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired MockMvc mockMvc;
  @Autowired CategoryRepository categoryRepository;

  private Long createCategory(String name) {
    Category category = new Category();
    category.setName(name);
    category.setDescription("Description for " + name);
    return categoryRepository.save(category).getId();
  }

  @Test
  void shouldCreateProduct_andReturn201_onProductController() throws Exception {
    Long categoryId = createCategory("Eletrônicos");
    ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
    payload.put("name", "Papel de Parede Modelo A");
    payload.put("description", "Papel de parede vinílico");
    payload.put("categoryId", categoryId);
    payload.put("baseUnitOfMeasurement", UomBase.M2.name());
    payload.put("buyUnitOfMeasurement", UomBuy.ROLL.name());
    payload.put("conversionBaseToBuy", 5.0);
    payload.put("minStock", 1);
    payload.put("maxStock", 20);
    payload.put("stockAlert", 1);

    mockMvc
        .perform(
            post("/api/product")
                .with(csrf())
                .header("Api-Version", "v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(payload)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(
            header().string("Location", org.hamcrest.Matchers.containsString("/api/product/")));
  }

  @Test
  void shouldGetAllProducts_andReturn200_onProductController() throws Exception {
    Long categoryId = createCategory("Papelaria");
    // Ensure at least one product exists
    ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
    payload.put("name", "Produto de Teste B");
    payload.put("description", "Descrição do Produto de Teste B");
    payload.put("categoryId", categoryId);
    payload.put("baseUnitOfMeasurement", UomBase.UN.name());
    payload.put("buyUnitOfMeasurement", UomBuy.UN.name());
    payload.put("conversionBaseToBuy", 1.0);
    payload.put("minStock", 1);
    payload.put("maxStock", 20);
    payload.put("stockAlert", 1);

    mockMvc
        .perform(
            post("/api/product")
                .with(csrf())
                .header("Api-Version", "v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(payload)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            get("/api/product/all")
                .param("role", "ADMIN")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
  }

  @Test
  void shouldGetAllProductsPaginated_andReturn200_onProductController() throws Exception {
    Long categoryId = createCategory("Móveis");
    // Create 2 products to test pagination
    for (int i = 0; i < 2; i++) {
      ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
      payload.put("name", "Produto Paginado " + i);
      payload.put("description", "Descrição " + i);
      payload.put("categoryId", categoryId);
      payload.put("baseUnitOfMeasurement", UomBase.UN.name());
      payload.put("buyUnitOfMeasurement", UomBuy.UN.name());
      payload.put("conversionBaseToBuy", 1.0);
      payload.put("minStock", 1);
      payload.put("maxStock", 20);
      payload.put("stockAlert", 1);

      mockMvc
          .perform(
              post("/api/product")
                  .with(csrf())
                  .header("Api-Version", "v1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(toJson(payload)))
          .andExpect(status().isCreated());
    }

    // Test first page with size 1
    mockMvc
        .perform(
            get("/api/product/all")
                .param("role", "ADMIN")
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    // Test second page with size 1
    mockMvc
        .perform(
            get("/api/product/all")
                .param("role", "ADMIN")
                .param("page", "1")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void shouldGetProductById_andReturn200_onProductController() throws Exception {
    Long categoryId = createCategory("Ferramentas");
    // Create a product first
    ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap<>();
    payload.put("name", "Produto para Buscar");
    payload.put("description", "Descrição do Produto para Buscar");
    payload.put("categoryId", categoryId);
    payload.put("baseUnitOfMeasurement", UomBase.UN.name());
    payload.put("buyUnitOfMeasurement", UomBuy.UN.name());
    payload.put("conversionBaseToBuy", 1.0);
    payload.put("minStock", 1);
    payload.put("maxStock", 20);
    payload.put("stockAlert", 1);

    String location =
        mockMvc
            .perform(
                post("/api/product")
                    .with(csrf())
                    .header("Api-Version", "v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(payload)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // If controller does not set Location header, fallback to fetch all and get last id via JSON
    // parsing
    long id;
    if (location != null && location.matches(".*/(\\d+)$")) {
      id = Long.parseLong(location.replaceAll(".*/(\\d+)$", "$1"));
    } else {
      var result =
          mockMvc
              .perform(
                  get("/api/product/all")
                      .param("role", "ADMIN")
                      .header("Api-Version", "v1")
                      .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andReturn();
      String responseJson = result.getResponse().getContentAsString();
      // Use ObjectMapper from AbstractIntegrationTest for robust parsing
      com.fasterxml.jackson.databind.ObjectMapper om =
          new com.fasterxml.jackson.databind.ObjectMapper();
      com.fasterxml.jackson.databind.JsonNode array = om.readTree(responseJson);
      com.fasterxml.jackson.databind.JsonNode last = array.get(array.size() - 1);
      id = last.get("id").asLong();
    }

    mockMvc
        .perform(
            get("/api/product/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn400_whenBodyIsMissing_onProductController() throws Exception {
    mockMvc
        .perform(
            post("/api/product")
                .with(csrf())
                .header("Api-Version", "v1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn403_whenRoleIsNotAdmin_onGetAllProducts() throws Exception {
    mockMvc
        .perform(
            get("/api/product/all")
                .param("role", "USER")
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isForbidden())
        .andExpect(content().string("Only ADMIN users can see list of products."));
  }

  @Test
  void shouldReturn403_whenRoleIsCustomer_onGetAllProducts() throws Exception {
    mockMvc
        .perform(
            get("/api/product/all")
                .param("role", "CUSTOMER")
                .accept(MediaType.APPLICATION_JSON)
                .header("Api-Version", "v1"))
        .andExpect(status().isForbidden())
        .andExpect(content().string("Only ADMIN users can see list of products."));
  }
}
