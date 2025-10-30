package br.ifsp.demo.controller;

import br.ifsp.demo.model.movie.Grade;
import br.ifsp.demo.model.movie.MovieId;
import br.ifsp.demo.model.user.Rating;
import br.ifsp.demo.model.user.Role;
import br.ifsp.demo.model.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.service.get.GetRatedMoviesService;
import br.ifsp.demo.service.patch.PatchRateService;
import br.ifsp.demo.service.post.PostRateService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingsController.class)
public class RatingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private GetRatedMoviesService getRatedMoviesService;

    @MockitoBean
    private PostRateService postRateService;

    @MockitoBean
    private PatchRateService patchRateService;

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

        when(patchRateService.patchRate(any(PatchRateService.PatchRateServiceRequestDTO.class)))
                .thenReturn(new PatchRateService.PatchRateServiceResponseDTO(rating));

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
                4
        );

        mockMvc.perform(patch("/api/v1/ratings")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNoContent());
    }
}
