package br.ifsp.demo.controller;

import br.ifsp.demo.model.user.Role;
import br.ifsp.demo.model.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.service.get.GetRecommendationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecommendationsController.class)
public class RecommendationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private GetRecommendationService getRecommendationService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaMovieRepository jpaMovieRepository;

    @MockitoBean
    private JpaUserRepository jpaUserRepository;

    @MockitoBean
    private CommandLineRunner commandLineRunner;

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-4]")
    @DisplayName("[SC-4.4] - Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/recommendations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 200 when getting recommendations successfully")
    void shouldReturn200WhenGettingRecommendations() throws Exception {
        java.util.UUID userId = java.util.UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(getRecommendationService.recommendMovies(any()))
                .thenReturn(new GetRecommendationService.RecommendationServiceResponseDTO(java.util.List.of()));

        mockMvc.perform(get("/api/v1/recommendations")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


