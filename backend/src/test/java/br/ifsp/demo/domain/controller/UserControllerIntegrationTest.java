package br.ifsp.demo.domain.controller;

import br.ifsp.demo.security.config.ApiExceptionHandler;
import br.ifsp.demo.domain.exception.EntityAlreadyExistsException;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.*;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.security.config.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import({SecurityConfiguration.class, ApiExceptionHandler.class})
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaUserRepository jpaUserRepository;

    @MockitoBean
    private CommandLineRunner commandLineRunner;

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register - Should return 201 when registering user successfully")
    void shouldReturn201WhenRegisteringUserSuccessfully() throws Exception {
        String jsonContent = """
                {
                    "name": "Lucas",
                    "lastname": "Silva",
                    "email": "lucas@gmail.com",
                    "password": "senha123"
                }
                """;

        UUID userId = UUID.randomUUID();
        RegisterUserResponse response = new RegisterUserResponse(userId);

        when(authenticationService.register(any(RegisterUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register - Should return 409 when email already exists")
    void shouldReturn409WhenRegisteringUserAlreadyExists() throws Exception {
        String jsonContent = """
                {
                    "name": "Lucas",
                    "lastname": "Greatest of All Time (G.O.A.T.)",
                    "email": "lucas@gmail.com",
                    "password": "senha"
                }
                """;

        String exceptionMessage = "Email already registered: lucas@gmail.com";

        when(authenticationService.register(any(RegisterUserRequest.class)))
                .thenThrow(new EntityAlreadyExistsException(exceptionMessage));

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exceptionMessage))
                .andExpect(jsonPath("$.status").value("CONFLICT"));
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register - Should return 400 when request body is empty")
    void shouldReturn400WhenRegisterRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register - Should return 400 when request body is malformed JSON")
    void shouldReturn400WhenRegisterRequestBodyIsMalformed() throws Exception {
        String malformedJson = "{ invalid json }";

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register - Should return 415 when content type is not JSON")
    void shouldReturn415WhenRegisterContentTypeIsNotJson() throws Exception {
        String content = "name=Lucas&email=lucas@gmail.com";

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content))
                .andExpect(status().isUnsupportedMediaType());
    }
    //------------------------------------------------------------------------------------------------
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /authenticate - Should return 200 when authentication is successful")
    void shouldReturn200WhenAuthenticationIsSuccessful() throws Exception {
        String jsonContent = """
                {
                    "email": "lucas@gmail.com",
                    "password": "senha123"
                }
                """;

        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
        AuthResponse response = new AuthResponse(jwtToken);

        when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /authenticate - Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        String jsonContent = """
                {
                    "email": "lucas@gmail.com",
                    "password": "wrongpassword"
                }
                """;

        when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /authenticate - Should return 400 when request body is empty")
    void shouldReturn400WhenAuthenticateRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /authenticate - Should return 400 when email is missing")
    void shouldReturn400WhenEmailIsMissing() throws Exception {
        String jsonContent = """
                {
                    "password": "senha123"
                }
                """;

        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

}