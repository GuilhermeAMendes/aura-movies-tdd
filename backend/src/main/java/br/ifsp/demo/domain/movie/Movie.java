package br.ifsp.demo.domain.movie;

import br.ifsp.demo.domain.movie.enums.Genre;
import br.ifsp.demo.domain.movie.valueObjects.MovieId;

public class Movie {
    private final MovieId id;
    private final String title;
    private final Genre genre;

    public Movie(MovieId id, String title, Genre genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public MovieId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }
}
