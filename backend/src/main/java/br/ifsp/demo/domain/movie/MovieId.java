package br.ifsp.demo.domain.movie;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
public class MovieId implements Serializable {
    private UUID id;

    protected MovieId() {}

    public MovieId(UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    public MovieId(String id) {
        this.id = UUID.fromString(Objects.requireNonNull(id));
    }

    public UUID unwrap() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieId movieId = (MovieId) o;
        return Objects.equals(id, movieId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}