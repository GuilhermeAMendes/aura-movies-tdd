package br.ifsp.demo.model.movie;

import br.ifsp.demo.exception.InvalidNoteInterval;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record Grade(int value) {
    public Grade {
        if (value < 0 || value > 5) throw new InvalidNoteInterval("Grade must be between 0 and 5");
    }

    @JsonCreator
    public static Grade of(String value) {
        return new Grade(Integer.parseInt(value));
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}