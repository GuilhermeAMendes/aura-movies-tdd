package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({IntegrationTestConfiguration.class, GetRatedMoviesServiceImpl.class})
@ActiveProfiles("test")
@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("GetRatedMoviesService Integration Tests with Database")
public class GetRatedMoviesServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private GetRatedMoviesServiceImpl ratedMoviesService;

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
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Action Movie", Genre.ACTION));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Comedy Movie", Genre.COMEDY));
        testMovies.add(new Movie(new MovieId(UUID.randomUUID()), "Drama Movie", Genre.DRAMA));

        testMovies.forEach(movie -> entityManager.persistAndFlush(movie));
        entityManager.persistAndFlush(testUser);
    }

    @Test
    @DisplayName("Should retrieve rated movies with join between user_ratings and movies tables")
    void shouldRetrieveRatedMoviesWithJoin() {
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovies.get(0).getMovieId(), new Grade(5));
        user.addRating(testMovies.get(1).getMovieId(), new Grade(4));
        user.addRating(testMovies.get(2).getMovieId(), new Grade(3));
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        var request = new GetRatedMoviesService.RatedServiceRequestDTO(testUser.getId());
        var response = ratedMoviesService.restoreRatedMovies(request);
        List<GetRatedMoviesService.RatedMovieDTO> ratedMovies = response.ratedMovies();

        assertThat(ratedMovies).hasSize(3);
        assertThat(ratedMovies).extracting(GetRatedMoviesService.RatedMovieDTO::movieId)
                .containsExactlyInAnyOrder(
                        testMovies.get(0).getMovieId(),
                        testMovies.get(1).getMovieId(),
                        testMovies.get(2).getMovieId()
                );

        GetRatedMoviesService.RatedMovieDTO firstRated = ratedMovies.stream()
                .filter(rm -> rm.movieId().equals(testMovies.get(0).getMovieId()))
                .findFirst()
                .orElseThrow();

        assertThat(firstRated.title()).isEqualTo("Action Movie");
        assertThat(firstRated.genre()).isEqualTo(Genre.ACTION);
        assertThat(firstRated.grade().value()).isEqualTo(5);
        assertThat(firstRated.lastGradedAt()).isNotNull();
    }
    @Test
    @DisplayName("Should return empty list when user has no ratings")
    void bshouldReturnEmptyListWhenUserHasNoRatings() {

        var request = new GetRatedMoviesService.RatedServiceRequestDTO(testUser.getId());
        var response = ratedMoviesService.restoreRatedMovies(request);
        List<GetRatedMoviesService.RatedMovieDTO> ratedMovies = response.ratedMovies();
        assertThat(ratedMovies).isEmpty();
    }
}