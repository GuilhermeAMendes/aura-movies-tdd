package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.Role;
import br.ifsp.demo.security.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import br.ifsp.demo.config.IntegrationTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({IntegrationTestConfiguration.class, GetRecommendationServiceImpl.class})
@ActiveProfiles("test")
@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("GetRecommendationService Integration Tests with Database")
public class GetRecommendationServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private GetRecommendationServiceImpl recommendationService;

    private User testUser;
    private List<Movie> testMovies;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password123")
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();

        testMovies = new ArrayList<>();
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Action Movie 1", Genre.ACTION));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Action Movie 2", Genre.ACTION));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Comedy Movie 1", Genre.COMEDY));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Comedy Movie 2", Genre.COMEDY));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Drama Movie 1", Genre.DRAMA));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Drama Movie 2", Genre.DRAMA));

        testMovies.forEach(movie -> entityManager.persistAndFlush(movie));
        entityManager.persistAndFlush(testUser);
    }

    @Test
    @DisplayName("Should filter recommendations by grade >= 4 aggregation from database")
    void shouldFilterRecommendationsByGradeGreaterThanOrEqual4() {
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovies.get(0).getMovieId(), new Grade(5));
        user.addRating(testMovies.get(1).getMovieId(), new Grade(4));
        user.addRating(testMovies.get(2).getMovieId(), new Grade(3));
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        var request = new GetRecommendationService.RecommendationServiceRequestDTO(testUser.getId());
        var response = recommendationService.recommendMovies(request);
        List<Movie> recommendations = response.recommendations();

        assertThat(recommendations).isNotEmpty();
        Set<Genre> recommendationGenres = recommendations.stream()
                .map(Movie::getGenre)
                .collect(Collectors.toSet());
        assertThat(recommendationGenres).contains(Genre.ACTION);
        assertThat(recommendations).extracting(Movie::getMovieId)
                .doesNotContain(testMovies.get(0).getMovieId(), testMovies.get(1).getMovieId());
        assertThat(recommendations).extracting(Movie::getGenre)
                .doesNotContain(Genre.COMEDY);
    }

    @Test
    @DisplayName("Should return all movies when user has no ratings")
    void shouldReturnAllMoviesWhenUserHasNoRatings() {
        var request = new GetRecommendationService.RecommendationServiceRequestDTO(testUser.getId());
        var response = recommendationService.recommendMovies(request);
        List<Movie> recommendations = response.recommendations();

        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations).extracting(Movie::getMovieId)
                .containsAll(testMovies.stream().map(Movie::getMovieId).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should aggregate preferred genres from positively rated movies")
    void shouldAggregatePreferredGenresFromPositivelyRatedMovies() {
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovies.get(0).getMovieId(), new Grade(5)); // ACTION
        user.addRating(testMovies.get(4).getMovieId(), new Grade(4)); // DRAMA
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        var request = new GetRecommendationService.RecommendationServiceRequestDTO(testUser.getId());
        var response = recommendationService.recommendMovies(request);
        List<Movie> recommendations = response.recommendations();

        Set<Genre> recommendationGenres = recommendations.stream()
                .map(Movie::getGenre)
                .collect(Collectors.toSet());
        assertThat(recommendationGenres).contains(Genre.ACTION, Genre.DRAMA);
        assertThat(recommendationGenres).doesNotContain(Genre.COMEDY);
    }

    @Test
    @DisplayName("Should exclude already rated movies from recommendations")
    void shouldExcludeAlreadyRatedMoviesFromRecommendations() {
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovies.get(0).getMovieId(), new Grade(5)); // Action Movie 1
        user.addRating(testMovies.get(1).getMovieId(), new Grade(4)); // Action Movie 2
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        var request = new GetRecommendationService.RecommendationServiceRequestDTO(testUser.getId());
        var response = recommendationService.recommendMovies(request);
        List<Movie> recommendations = response.recommendations();

        assertThat(recommendations).extracting(Movie::getMovieId)
                .doesNotContain(testMovies.get(0).getMovieId(), testMovies.get(1).getMovieId());
    }
    @Test
    @DisplayName("Should handle grade exactly 4 as positive rating")
    void shouldHandleGradeExactly4AsPositiveRating() {
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovies.get(0).getMovieId(), new Grade(4)); // Exactly 4
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        var request = new GetRecommendationService.RecommendationServiceRequestDTO(testUser.getId());
        var response = recommendationService.recommendMovies(request);
        List<Movie> recommendations = response.recommendations();

        assertThat(recommendations).isNotEmpty();
        Set<Genre> recommendationGenres = recommendations.stream()
                .map(Movie::getGenre)
                .collect(Collectors.toSet());
        assertThat(recommendationGenres).contains(Genre.ACTION);
    }
}