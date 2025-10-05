package br.ifsp.demo.domain.movie;

import br.ifsp.demo.exception.InvalidNoteInterval;
import jakarta.persistence.Embeddable;

@Embeddable
public record Grade(int value) {
    public Grade {
        if (value < 0 || value > 5) throw new InvalidNoteInterval("Grade must be between 0 and 5");
    }
}