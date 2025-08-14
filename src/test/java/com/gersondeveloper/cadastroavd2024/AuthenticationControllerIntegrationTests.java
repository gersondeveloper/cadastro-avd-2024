package com.gersondeveloper.cadastroavd2024;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerIntegrationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    static MSSQLServerContainer mssqlserverContainer = new MSSQLServerContainer()
//            .acceptLicense();
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.url", mssqlserverContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mssqlserverContainer::getUsername);
//        registry.add("spring.datasource.password", mssqlserverContainer::getPassword);
//    }
//
//    @BeforeAll
//    static void beforeAll(){
//        mssqlserverContainer.start();
//    }
//
//    @AfterAll
//    static void afterAll(){
//        mssqlserverContainer.stop();
//    }
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void testSuccessfulRegisterEndpoint() throws Exception {
//        // Prepare request body
//        UserRegisterRequestDto registerRequest = new UserRegisterRequestDto("teste@teste.com", "Gerson Filho", "password", UserRole.ADMIN);
//        String requestBody = objectMapper.writeValueAsString(registerRequest);
//
//        // Perform POST request to /register endpoint
//        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    public void testUnSuccessfulRegisterEndpoint() throws Exception {
//        // Prepare request body
//        UserRegisterRequestDto registerRequest = new UserRegisterRequestDto("teste@teste.com", "","password", null);
//        String requestBody = objectMapper.writeValueAsString(registerRequest);
//
//        // Perform POST request to /register endpoint
//        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    public void testLoginSuccessfulEndpoint() throws Exception{
//        // Prepare register user
//        UserRegisterRequestDto registerRequest = new UserRegisterRequestDto("teste@teste.com","Gerson Filho", "password", UserRole.ADMIN);
//        String requestBody = objectMapper.writeValueAsString(registerRequest);
//
//        // Prepare login
//        UserAuthenticationRequestDto userAuthenticationRequestDto = new UserAuthenticationRequestDto("teste@teste.com", "password");
//        String requestLoginBody = objectMapper.writeValueAsString(userAuthenticationRequestDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestLoginBody))
//                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//    }
//
//    @Test
//    public void testLoginUnsuccessfulEndpoint() throws Exception{
//        // Prepare register user
//        UserRegisterRequestDto registerRequest = new UserRegisterRequestDto("teste@teste.com","Gerson Filho", "password2", UserRole.ADMIN);
//        String requestBody = objectMapper.writeValueAsString(registerRequest);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//
//        // Prepare login
//        UserAuthenticationRequestDto userAuthenticationRequestDto = new UserAuthenticationRequestDto("teste3434@teste.com", "password");
//        String requestLoginBody = objectMapper.writeValueAsString(userAuthenticationRequestDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestLoginBody))
//                .andExpect(MockMvcResultMatchers.status().isForbidden());
//
//    }

}
