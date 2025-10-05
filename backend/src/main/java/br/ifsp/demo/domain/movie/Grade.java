package br.ifsp.demo.domain.movie;

import jakarta.persistence.Embeddable;

@Embeddable
public record Grade(int value) {
    public Grade {
        if (value < 0 || value > 5) throw new IllegalArgumentException("Grade must be between 0 and 5");
    }
}