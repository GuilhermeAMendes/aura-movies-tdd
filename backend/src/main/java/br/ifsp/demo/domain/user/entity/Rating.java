package br.ifsp.demo.domain.user.entity;

import br.ifsp.demo.domain.movie.valueObjects.MovieId;
import br.ifsp.demo.domain.movie.valueObjects.Note;

import java.time.LocalDateTime;
import java.util.UUID;

public class Rating {
    private final UUID id;
    private final MovieId movieId;
    private Note note;
    private LocalDateTime dateOfRating;

    public Rating(UUID id, MovieId movieId, Note note, LocalDateTime dateOfRating) {
        this.id = id;
        this.movieId = movieId;
        this.note = note;
        this.dateOfRating = dateOfRating;
    }

    public UUID getId() {
        return id;
    }

    public MovieId getMovieId() {
        return movieId;
    }

    public Note getNote() {
        return note;
    }

    public LocalDateTime getDateOfRating() {
        return dateOfRating;
    }

}
