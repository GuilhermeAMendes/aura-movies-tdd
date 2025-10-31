package br.ifsp.demo.domain.controller;

import br.ifsp.demo.domain.exception.ApiExceptionHandler;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingsController.class)
@Import({SecurityConfiguration.class, ApiExceptionHandler.class})
public class RatingsControllerTest {

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

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-2]")
    @DisplayName("[SC-2.4] - Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-1]")
    @DisplayName("[SC-1.5] - Should return 401 when posting a rating without auth")
    void shouldReturnUnauthorizedWhenPostingRatingWithoutAuth() throws Exception {
        String ratingBody = "{}";
        mockMvc.perform(get("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON).content(ratingBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 200 when getting rated movies successfully")
    void shouldReturn200WhenGettingRatedMovies() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(getRatedMoviesService.restoreRatedMovies(any(GetRatedMoviesService.RatedServiceRequestDTO.class)))
                .thenReturn(new GetRatedMoviesService.RatedServiceResponseDTO(List.of()));

        mockMvc.perform(get("/api/v1/ratings")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 201 when posting rating successfully")
    void shouldReturn201WhenPostingRating() throws Exception {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Grade grade = new Grade(5);
        Rating rating = new Rating(movieId, grade, LocalDateTime.now());

        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(postRateService.saveRate(any(PostRateService.PostRateServiceRequestDTO.class)))
                .thenReturn(new PostRateService.PostRateServiceResponseDTO(rating));

        // Create JSON manually with grade as string because @JsonCreator expects String
        String jsonContent = String.format(
                """
                        {
                          "userId": "%s",
                          "movieId": {
                            "id": "%s"
                          },
                          "grade": "%d"
                        }""",
                userId,
                movieId.unwrap(),
                5
        );

        mockMvc.perform(post("/api/v1/ratings")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 204 when patching rating successfully")
    void shouldReturn204WhenPatchingRating() throws Exception {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Grade grade = new Grade(4);
        Rating rating = new Rating(movieId, grade, LocalDateTime.now());

        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(authenticationInfoService.getAuthenticatedUserId()).thenReturn(userId);

        when(patchRateService.patchRate(any(PatchRateService.PatchRateServiceRequestDTO.class)))
                .thenReturn(new PatchRateService.PatchRateServiceResponseDTO(rating));

        String jsonContent = String.format(
                """
                        {
                          "grade": "%d"
                        }""",
                grade.value()
        );

        mockMvc.perform(patch("/api/v1/ratings/{movieId}", movieId.unwrap())
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 204 when delete rate movie with successfully")
    void shouldReturn204WhenDeleteRatingMovie() throws Exception {
        MovieId movieId = new MovieId(UUID.randomUUID());
        UUID userId = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        mockMvc.perform(delete("/api/v1/ratings/{movieId}", movieId.unwrap())
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 404 when delete rate movie is not found")
    void shouldReturn404WhenDeleteRatingMovieNotFound() throws Exception {
        MovieId movieId = new MovieId(UUID.randomUUID());
        UUID userId = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        String exceptionMessage = "Movie not found";

        when(authenticationInfoService.getAuthenticatedUserId()).thenReturn(userId);

        doThrow(new MovieNotFoundException(exceptionMessage))
                .when(deleteRateService)
                .deleteRate(any(DeleteRateService.DeleteRateServiceRequestDTO.class));

        mockMvc.perform(delete("/api/v1/ratings/{movieId}", movieId.unwrap())
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exceptionMessage))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return 403 when operation is forbidden")
    void shouldReturn403WhenOperationIsForbidden() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        String exceptionMessage = "Ilegal State";

        when(authenticationInfoService.getAuthenticatedUserId()).thenReturn(userId);

        when(getRatedMoviesService.restoreRatedMovies(any(GetRatedMoviesService.RatedServiceRequestDTO.class)))
                .thenThrow(new IllegalStateException(exceptionMessage));

        mockMvc.perform(get("/api/v1/ratings")
        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
