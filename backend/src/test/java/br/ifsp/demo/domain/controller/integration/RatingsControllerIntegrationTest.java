package br.ifsp.demo.domain.controller;

import br.ifsp.demo.security.config.ApiExceptionHandler;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.domain.service.delete.DeleteRateService;
import br.ifsp.demo.domain.service.get.GetRatedMoviesService;
import br.ifsp.demo.domain.service.patch.PatchRateService;
import br.ifsp.demo.domain.service.post.PostRateService;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.auth.Role;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.security.config.SecurityConfiguration;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingsController.class)
@Import({SecurityConfiguration.class, ApiExceptionHandler.class})
public class RatingsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private GetRatedMoviesService getRatedMoviesService;

    @MockitoBean
    private PostRateService postRateService;

    @MockitoBean
    private PatchRateService patchRateService;

    @MockitoBean
    private DeleteRateService deleteRateService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaMovieRepository jpaMovieRepository;

    @MockitoBean
    private JpaUserRepository jpaUserRepository;

    @MockitoBean
    private CommandLineRunner commandLineRunner;

    private User createUserMock() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /ratings - Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /ratings - Should return 200 with rated movies when getting ratings successfully")
    void shouldReturn200WhenGettingRatedMovies() throws Exception {
        User mockUser = createUserMock();

        when(getRatedMoviesService.restoreRatedMovies(any(GetRatedMoviesService.RatedServiceRequestDTO.class)))
                .thenReturn(new GetRatedMoviesService.RatedServiceResponseDTO(List.of()));

        mockMvc.perform(get("/api/v1/ratings")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratedMovies").exists());
    }

}