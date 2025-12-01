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

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies/{id} - Should return 401 when user is not authenticated")
    void shouldReturn401WhenNotAuthenticatedForGetMovieById() throws Exception {
        mockMvc.perform(get("/api/v1/movies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies/{id} - Should return 404 when movie is not found")
    void shouldReturn404WhenMovieIsNotFound() throws Exception {
        UUID movieId = UUID.randomUUID();
        User mockUser = createMockUser();
        String exceptionMessage = "Movie not found";

        when(getMovieByIdService.getMovieById(any(GetMovieByIdService.GetMovieByIdRequestDTO.class)))
                .thenThrow(new MovieNotFoundException(exceptionMessage));

        mockMvc.perform(get("/api/v1/movies/{id}", movieId)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exceptionMessage))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies/{id} - Should return 400 when movie id is invalid format")
    void shouldReturn400WhenMovieIdIsInvalidFormat() throws Exception {
        User mockUser = createMockUser();

        mockMvc.perform(get("/api/v1/movies/{id}", "invalid-uuid")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies - Should return 200 with list of movies when getting all movies successfully")
    void shouldReturn200WhenGettingAllMovies() throws Exception {
        User mockUser = createMockUser();
        Movie movie1 = new Movie(new MovieId(UUID.randomUUID()), "Movie 1", Genre.ACTION);
        Movie movie2 = new Movie(new MovieId(UUID.randomUUID()), "Movie 2", Genre.COMEDY);
        List<Movie> movies = List.of(movie1, movie2);

        when(getAllMoviesService.getAllMovies(any(GetAllMoviesService.GetAllMoviesRequestDTO.class)))
                .thenReturn(new GetAllMoviesService.GetAllMoviesResponseDTO(movies));

        mockMvc.perform(get("/api/v1/movies")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies.size()").value(2))
                .andExpect(jsonPath("$.movies[0].title").value(movie1.getTitle()))
                .andExpect(jsonPath("$.movies[0].genre").value(Genre.ACTION.toString()))
                .andExpect(jsonPath("$.movies[1].title").value(movie2.getTitle()))
                .andExpect(jsonPath("$.movies[1].movieId.id").value(movie2.getMovieId().unwrap().toString()));
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies - Should return 200 with empty list when no movies exist")
    void shouldReturn200WithEmptyListWhenNoMoviesExist() throws Exception {
        User mockUser = createMockUser();

        when(getAllMoviesService.getAllMovies(any(GetAllMoviesService.GetAllMoviesRequestDTO.class)))
                .thenReturn(new GetAllMoviesService.GetAllMoviesResponseDTO(List.of()));

        mockMvc.perform(get("/api/v1/movies")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies").isArray())
                .andExpect(jsonPath("$.movies").isEmpty());
    }
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /movies - Should return 401 when user is not authenticated")
    void shouldReturn401WhenNotAuthenticatedForGetAllMovies() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}