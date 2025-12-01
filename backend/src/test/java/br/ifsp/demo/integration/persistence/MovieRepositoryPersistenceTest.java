package br.ifsp.demo.integration.persistence;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.utils.EntityBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MovieRepositoryPersistenceTest.TestConfig.class)
public class MovieRepositoryPersistenceTest {

    @Autowired
    private JpaMovieRepository movieRepository;

    @Test
    @DisplayName("Should save movie successfully")
    void shouldSaveMovie() {
        Movie movie = EntityBuilder.createRandomMovie();

        Movie saved = movieRepository.save(movie);

        assertThat(saved.getMovieId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("Should fail when title is null")
    void shouldFailWhenTitleIsNull() {
        Movie movie = new Movie(
                new MovieId(java.util.UUID.randomUUID()),
                null,
                Genre.SCI_FI
        );

        assertThatThrownBy(() -> movieRepository.save(movie))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}