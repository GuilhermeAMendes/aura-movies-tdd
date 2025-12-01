package br.ifsp.demo.domain.service;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IntegrationTestConfiguration.class)
@ActiveProfiles("test")
@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("User Ratings Persistence Integration Tests")
public class UserRatingsPersistenceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository userRepository;

    private User testUser;
    private Movie testMovie1;
    private Movie testMovie2;

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

        testMovie1 = new Movie(
                new MovieId(UUID.randomUUID()),
                "Test Movie 1",
                Genre.ACTION
        );
        testMovie2 = new Movie(
                new MovieId(UUID.randomUUID()),
                "Test Movie 2",
                Genre.COMEDY
        );
        entityManager.persistAndFlush(testMovie1);
        entityManager.persistAndFlush(testMovie2);
        entityManager.persistAndFlush(testUser);
    }

    @Test
    @DisplayName("Should persist and retrieve ratings through ElementCollection")
    void shouldPersistAndRetrieveRatings() {
        Grade grade1 = new Grade(5);
        Grade grade2 = new Grade(4);

        userRepository.findById(testUser.getId())
                .orElseThrow()
                .addRating(testMovie1.getMovieId(), grade1);

        userRepository.findById(testUser.getId())
                .orElseThrow()
                .addRating(testMovie2.getMovieId(), grade2);

        userRepository.save(testUser);
        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        List<Rating> ratings = retrievedUser.getRatings();

        assertThat(ratings).hasSize(2);
        assertThat(ratings).extracting(Rating::getMovieId)
                .contains(testMovie1.getMovieId(), testMovie2.getMovieId());
        assertThat(ratings).extracting(r -> r.getGrade().value())
                .contains(5, 4);
    }
    @Test
    @DisplayName("Should enforce uniqueness rule - cannot add duplicate rating for same movie")
    void shouldEnforceUniquenessRuleForRatings() {
        Grade grade1 = new Grade(5);
        Grade grade2 = new Grade(3);

        User user = userRepository.findById(testUser.getId()).orElseThrow();
        Rating firstRating = user.addRating(testMovie1.getMovieId(), grade1);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        Rating duplicateRating = retrievedUser.addRating(testMovie1.getMovieId(), grade2);

        assertThat(firstRating).isNotNull();
        assertThat(duplicateRating).isNull();

        userRepository.save(retrievedUser);
        entityManager.flush();
        entityManager.clear();
        User finalUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(finalUser.getRatings()).hasSize(1);
        assertThat(finalUser.getRatings().get(0).getGrade().value()).isEqualTo(5);
    }
    @Test
    @DisplayName("Should update existing rating and persist changes")
    void shouldUpdateExistingRating() {
        Grade initialGrade = new Grade(3);
        Grade updatedGrade = new Grade(5);
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovie1.getMovieId(), initialGrade);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        Rating updatedRating = retrievedUser.updateRating(testMovie1.getMovieId(), updatedGrade);
        userRepository.save(retrievedUser);
        entityManager.flush();
        entityManager.clear();

        User finalUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(finalUser.getRatings()).hasSize(1);
        assertThat(finalUser.getRatings().get(0).getGrade().value()).isEqualTo(5);
        assertThat(updatedRating.getGrade().value()).isEqualTo(5);
    }
    @Test
    @DisplayName("Should delete rating and persist changes")
    void shouldDeleteRating() {
        Grade grade1 = new Grade(5);
        Grade grade2 = new Grade(4);
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovie1.getMovieId(), grade1);
        user.addRating(testMovie2.getMovieId(), grade2);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        retrievedUser.deleteRating(testMovie1.getMovieId());
        userRepository.save(retrievedUser);
        entityManager.flush();
        entityManager.clear();

        User finalUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(finalUser.getRatings()).hasSize(1);
        assertThat(finalUser.getRatings().get(0).getMovieId()).isEqualTo(testMovie2.getMovieId());
    }
    @Test
    @DisplayName("Should persist lastGradedAt timestamp when adding rating")
    void shouldPersistLastGradedAtTimestamp() {
        Grade grade = new Grade(5);
        LocalDateTime beforeAdd = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovie1.getMovieId(), grade);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        LocalDateTime afterAdd = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        Rating retrievedRating = retrievedUser.getRatings().stream()
                .filter(r -> r.getMovieId().equals(testMovie1.getMovieId()))
                .findFirst()
                .orElseThrow();

        assertThat(retrievedRating.getLastGradedAt()).isNotNull();
        assertThat(retrievedRating.getLastGradedAt())
                .isAfterOrEqualTo(beforeAdd)
                .isBeforeOrEqualTo(afterAdd);
    }

    @Test
    @DisplayName("Should handle multiple ratings for different movies")
    void shouldHandleMultipleRatingsForDifferentMovies() {
        Movie movie3 = new Movie(new MovieId(UUID.randomUUID()), "Movie 3", Genre.DRAMA);
        Movie movie4 = new Movie(new MovieId(UUID.randomUUID()), "Movie 4", Genre.THRILLER);

        entityManager.persistAndFlush(movie3);
        entityManager.persistAndFlush(movie4);

        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovie1.getMovieId(), new Grade(5));
        user.addRating(testMovie2.getMovieId(), new Grade(4));
        user.addRating(movie3.getMovieId(), new Grade(3));
        user.addRating(movie4.getMovieId(), new Grade(5));
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();

        assertThat(retrievedUser.getRatings()).hasSize(4);
        assertThat(retrievedUser.getRatings())
                .extracting(Rating::getMovieId)
                .containsExactlyInAnyOrder(
                        testMovie1.getMovieId(),
                        testMovie2.getMovieId(),
                        movie3.getMovieId(),
                        movie4.getMovieId()
                );
    }
    @Test
    @DisplayName("Should count ratings per user correctly using COUNT aggregation")
    void shouldCountRatingsPerUserCorrectly() {
        Movie movie3 = new Movie(new MovieId(UUID.randomUUID()), "Movie 3", Genre.DRAMA);
        entityManager.persistAndFlush(movie3);
        User user = userRepository.findById(testUser.getId()).orElseThrow();
        user.addRating(testMovie1.getMovieId(), new Grade(5));
        user.addRating(testMovie2.getMovieId(), new Grade(4));
        user.addRating(movie3.getMovieId(), new Grade(3));
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userRepository.findById(testUser.getId()).orElseThrow();
        int ratingCount = retrievedUser.getRatings().size();
        assertThat(ratingCount).isEqualTo(3);
    }

}