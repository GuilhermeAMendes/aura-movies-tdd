package br.ifsp.demo.domain.model.movie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieIdTest {

    @Test
    @Tag("UnitTest")
    @DisplayName("Should check method equals of Movie Id value object")
    void testEquals() {
        UUID id = UUID.randomUUID();
        MovieId movieId1 = new MovieId(id);
        MovieId movieId2 = new MovieId(id);
        MovieId movieId3 = new MovieId(UUID.randomUUID());

        assertThat(movieId1.equals(movieId2)).isTrue();
        assertThat(movieId1.equals(movieId3)).isFalse();
        assertThat(movieId1.equals(null)).isFalse();
        assertThat(movieId1.equals(new Object())).isFalse();
        assertThat(movieId1.hashCode()).isEqualTo(movieId2.hashCode());
        assertThat(movieId1.hashCode()).isNotEqualTo(movieId3.hashCode());
    }
}
