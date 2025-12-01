package br.ifsp.demo.domain.repository;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
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
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IntegrationTestConfiguration.class)
@ActiveProfiles("test")
@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("JpaMovieRepository Integration Tests")
public class JpaMovieRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaMovieRepository movieRepository;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = new Movie(
                new MovieId(UUID.randomUUID()),
                "Test Movie",
                Genre.ACTION
        );
    }

    @Test
    @DisplayName("Should persist and retrieve movie successfully")
    void shouldPersistAndRetrieveMovie() {
        entityManager.persistAndFlush(testMovie);
        var found = movieRepository.findById(testMovie.getMovieId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Movie");
        assertThat(found.get().getGenre()).isEqualTo(Genre.ACTION);
    }
    @Test
    @DisplayName("Should enforce NOT NULL constraint on title field")
    void shouldEnforceNotNullConstraintOnTitle() {
        Movie movieWithoutTitle = new Movie(
                new MovieId(UUID.randomUUID()),
                null,
                Genre.ACTION
        );

        assertThatThrownBy(() -> {
            entityManager.persist(movieWithoutTitle);
            entityManager.flush();
        }).isInstanceOfAny(DataIntegrityViolationException.class, PersistenceException.class);
    }
    @Test
    @DisplayName("Should enforce NOT NULL constraint on genre field")
    void shouldEnforceNotNullConstraintOnGenre() {
        Movie movieWithoutGenre = new Movie(
                new MovieId(UUID.randomUUID()),
                "Test Movie",
                null
        );
        assertThatThrownBy(() -> {
            entityManager.persist(movieWithoutGenre);
            entityManager.flush();
        }).isInstanceOfAny(DataIntegrityViolationException.class, PersistenceException.class);
    }
    @Test
    @DisplayName("Should retrieve all movies using inherited findAll method")
    void shouldRetrieveAllMovies() {
        Movie movie1 = new Movie(new MovieId(UUID.randomUUID()), "Movie 1", Genre.ACTION);
        Movie movie2 = new Movie(new MovieId(UUID.randomUUID()), "Movie 2", Genre.COMEDY);
        Movie movie3 = new Movie(new MovieId(UUID.randomUUID()), "Movie 3", Genre.DRAMA);

        entityManager.persistAndFlush(movie1);
        entityManager.persistAndFlush(movie2);
        entityManager.persistAndFlush(movie3);

        List<Movie> allMovies = movieRepository.findAll();
        assertThat(allMovies).hasSizeGreaterThanOrEqualTo(3);
        assertThat(allMovies).extracting(Movie::getTitle)
                .contains("Movie 1", "Movie 2", "Movie 3");
    }
    @Test
    @DisplayName("Should count movies correctly")
    void shouldCountMovies() {
        Movie movie1 = new Movie(new MovieId(UUID.randomUUID()), "Movie 1", Genre.ACTION);
        Movie movie2 = new Movie(new MovieId(UUID.randomUUID()), "Movie 2", Genre.COMEDY);

        entityManager.persistAndFlush(movie1);
        entityManager.persistAndFlush(movie2);
        long count = movieRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}