package br.ifsp.demo.domain.repository;

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
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IntegrationTestConfiguration.class)
@ActiveProfiles("test")
@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("JpaUserRepository Integration Tests")
public class JpaUserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password123")
                .role(Role.USER)
                .ratings(new java.util.ArrayList<>())
                .build();
    }
    @Test
    @DisplayName("Should find user by email using custom query")
    void shouldFindUserByEmail() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find user by ID using custom query")
    void shouldFindUserById() {
        UUID userId = testUser.getId();
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findUserById(userId);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(userId);
    }
    @Test
    @DisplayName("Should return empty when user ID not found")
    void shouldReturnEmptyWhenUserIdNotFound() {
        Optional<User> found = userRepository.findUserById(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should enforce NOT NULL constraint on name field")
    void shouldEnforceNotNullConstraintOnName() {
        User userWithoutName = User.builder()
                .id(UUID.randomUUID())
                .name(null)
                .lastname("User")
                .email("test2@example.com")
                .password("password123")
                .role(Role.USER)
                .ratings(new java.util.ArrayList<>())
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(userWithoutName);
            entityManager.flush();
        }).isInstanceOfAny(DataIntegrityViolationException.class, PersistenceException.class);
    }

}
