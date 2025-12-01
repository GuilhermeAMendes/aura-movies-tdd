package br.ifsp.demo.domain.controller;

import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.domain.service.get.GetAllMoviesService;
import br.ifsp.demo.domain.service.get.GetMovieByIdService;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.auth.Role;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.security.config.ApiExceptionHandler;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
@Import({SecurityConfiguration.class, ApiExceptionHandler.class})
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private GetMovieByIdService getMovieByIdService;

    @MockitoBean
    private GetAllMoviesService getAllMoviesService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JpaMovieRepository jpaMovieRepository;

    @MockitoBean
    private JpaUserRepository jpaUserRepository;

    @MockitoBean
    private CommandLineRunner commandLineRunner;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    private User createMockUser() {
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
    @DisplayName("GET /movies/{id} - Should return 200 with movie data when getting movie by id successfully")
    void shouldReturn200WhenGettingMovieById() throws Exception {
        User mockUser = createMockUser();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Movie movie = new Movie(movieId, "Test Movie", Genre.ACTION);

        when(getMovieByIdService.getMovieById(any(GetMovieByIdService.GetMovieByIdRequestDTO.class)))
                .thenReturn(new GetMovieByIdService.GetMovieByIdResponseDTO(movie, null));

        mockMvc.perform(get("/api/v1/movies/{id}", movieId.unwrap())
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.movie.genre").value(movie.getGenre().toString()))
                .andExpect(jsonPath("$.movie.movieId.id").value(movieId.unwrap().toString()))
                .andExpect(jsonPath("$.rating").isEmpty());
    }
}