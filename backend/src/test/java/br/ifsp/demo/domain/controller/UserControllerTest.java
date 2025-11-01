package br.ifsp.demo.domain.controller;

import br.ifsp.demo.security.config.ApiExceptionHandler;
import br.ifsp.demo.domain.exception.EntityAlreadyExistsException;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.auth.AuthenticationService;
import br.ifsp.demo.security.auth.RegisterUserRequest;
import br.ifsp.demo.security.auth.UserController;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.security.config.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import({SecurityConfiguration.class, ApiExceptionHandler.class})
public class UserControllerTest {

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
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 409 when registering user already exists")
    void shouldReturn409WhenRegisteringUserAlreadyExists() throws Exception {
        String jsonContent =
                """
                  {
                          "name": "Lucas",
                          "lastname": "Greatest of All Time (G.O.A.T.)",
                          "email": "lucas@gmail.com",
                          "password": "senha"
                        }""";

        String exceptionMessage = "Email already registered: lucas@gmail.com";

        when(authenticationService.register(any(RegisterUserRequest.class)))
                .thenThrow(new EntityAlreadyExistsException(exceptionMessage));

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exceptionMessage))
                .andExpect(jsonPath("$.status").value("CONFLICT"));
    }

}
