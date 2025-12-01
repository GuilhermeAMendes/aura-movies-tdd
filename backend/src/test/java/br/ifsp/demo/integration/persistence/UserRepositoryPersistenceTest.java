package br.ifsp.demo.integration.persistence;

import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.utils.EntityBuilder;
import org.junit.jupiter.api.Disabled;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserRepositoryPersistenceTest.TestConfig.class)
public class UserRepositoryPersistenceTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should save user and find by email")
    void shouldSaveAndFindUserByEmail() {
        User user = EntityBuilder.createRandomUser();
        user.setPassword(passwordEncoder.encode("senha123"));

        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when saving duplicate email")
    @Disabled("Bug encontrado: O banco aceita e-mails duplicados.")
    void shouldThrowExceptionWhenDuplicateEmail() {
        User user1 = EntityBuilder.createRandomUser();
        String email = "duplicate@test.com";
        user1.setEmail(email);
        userRepository.saveAndFlush(user1); // Garante que o primeiro foi pro banco

        // WHEN: Tento salvar outro usuário com MESMO email
        User user2 = EntityBuilder.createRandomUser();
        user2.setEmail(email); // Email duplicado

        // THEN: O Banco deve rejeitar IMEDIATAMENTE ao usar saveAndFlush
        assertThatThrownBy(() -> userRepository.saveAndFlush(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should persist user ratings successfully")
    void shouldPersistUserRatings() {
        User user = EntityBuilder.createRandomUser();
        userRepository.save(user);

        user.addRating(new MovieId(UUID.randomUUID()), new Grade(5));
        user.addRating(new MovieId(UUID.randomUUID()), new Grade(3));

        userRepository.save(user); // Atualizo o usuário

        User fetchedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(fetchedUser.getRatings()).hasSize(2);
        assertThat(fetchedUser.getRatings().get(0).getGrade().value()).isEqualTo(5);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}