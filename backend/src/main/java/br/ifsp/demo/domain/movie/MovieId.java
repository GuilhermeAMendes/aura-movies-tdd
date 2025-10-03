package br.ifsp.demo.domain.movie;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MovieId implements Serializable {
    private final UUID id;

    public MovieId(UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    public UUID unwrap() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovieId movieId = (MovieId) o;
        return Objects.equals(id, movieId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
