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
import org.springframework.boot.test.context.TestConfiguration; // Import Novo
import org.springframework.context.annotation.Bean; // Import Novo
import org.springframework.context.annotation.Import; // Import Novo
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import Novo
import org.springframework.security.crypto.password.PasswordEncoder; // Import Novo

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MovieRepositoryPersistenceTest.TestConfig.class) // <--- Adicione esta linha para importar a config abaixo
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
        // GIVEN: Um filme COM ID, mas SEM Título
        Movie movie = new Movie(
                new MovieId(java.util.UUID.randomUUID()), // Passamos um ID válido
                null, // Título inválido (nulo)
                Genre.SCI_FI
        );

        // WHEN / THEN
        // Agora sim, esperamos que o banco reclame do Título, não do ID
        assertThatThrownBy(() -> movieRepository.save(movie))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    /**
     * Esta configuração interna fornece o Bean que estava faltando (PasswordEncoder).
     * Ela só existe dentro deste teste, resolvendo o erro do 'initData'.
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}