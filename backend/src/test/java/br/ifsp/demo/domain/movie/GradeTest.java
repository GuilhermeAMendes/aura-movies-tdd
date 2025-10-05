package br.ifsp.demo.domain.movie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GradeTest {

    @Tag("UnitTest")
    @Tag("[US-3]")
    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 6, 10})
    @DisplayName("[SC-3.2] - Should throw exception when grade is out of interval [0, 5]")
    void shouldThrowExceptionWhenGradeIsOutOfInterval(int invalidGradeValue) {
        assertThatThrownBy(() -> new Grade(invalidGradeValue)).isInstanceOf(IllegalArgumentException.class);
    }
}