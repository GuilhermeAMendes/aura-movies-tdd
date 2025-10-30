package br.ifsp.demo.domain.model.movie;

import br.ifsp.demo.domain.model.movie.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GradeTest {

    @Tag("UnitTest")
    @Tag("[US-1]")
    @Tag("[US-3]")
    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 6, 10})
    @DisplayName("[SC-1.2, SC-3.2] - Should throw exception when grade is out of interval [0, 5]")
    void shouldThrowExceptionWhenGradeIsOutOfInterval(int invalidGradeValue) {
        assertThatThrownBy(() -> new Grade(invalidGradeValue)).isInstanceOf(IllegalArgumentException.class);
    }

    @Tag("UnitTest")
    @Tag("[US-3]")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Should create grade when value is within interval [0, 5]")
    void shouldCreateGradeWhenValueIsWithinInterval(int validGradeValue) {
        Grade grade = new Grade(validGradeValue);
        assertThat(grade.value()).isEqualTo(validGradeValue);
    }

}