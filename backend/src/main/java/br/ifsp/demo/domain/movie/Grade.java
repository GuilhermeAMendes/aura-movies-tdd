package br.ifsp.demo.domain.movie;

import jakarta.persistence.Embeddable;

@Embeddable
public record Grade(int value) {
    public Grade() {
        this(0);
    }
}