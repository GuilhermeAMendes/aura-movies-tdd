package br.ifsp.demo.controller;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Role;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.service.get.GetAllMoviesService;
import br.ifsp.demo.service.get.GetMovieByIdService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest {

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

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 200 when getting movie by id successfully")
    void shouldReturn200WhenGettingMovieById() throws Exception {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Movie movie = new Movie(movieId, "Test Movie", Genre.ACTION);

        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(getMovieByIdService.getMovieById(any(GetMovieByIdService.GetMovieByIdRequestDTO.class)))
                .thenReturn(new GetMovieByIdService.GetMovieByIdResponseDTO(movie));

        mockMvc.perform(get("/api/v1/movies/{id}", movieId.unwrap())
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 200 when getting all movies successfully")
    void shouldReturn200WhenGettingAllMovies() throws Exception {
        UUID userId = UUID.randomUUID();
        List<Movie> movies = List.of(
                new Movie(new MovieId(UUID.randomUUID()), "Movie 1", Genre.ACTION),
                new Movie(new MovieId(UUID.randomUUID()), "Movie 2", Genre.COMEDY)
        );

        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(getAllMoviesService.getAllMovies(any(GetAllMoviesService.GetAllMoviesRequestDTO.class)))
                .thenReturn(new GetAllMoviesService.GetAllMoviesResponseDTO(movies));

        mockMvc.perform(get("/api/v1/movies")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 401 when user is not authenticated for getMovieById")
    void shouldReturn401WhenNotAuthenticatedForGetMovieById() throws Exception {
        mockMvc.perform(get("/api/v1/movies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 401 when user is not authenticated for getAllMovies")
    void shouldReturn401WhenNotAuthenticatedForGetAllMovies() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
