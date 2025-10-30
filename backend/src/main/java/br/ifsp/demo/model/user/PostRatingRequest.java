package br.ifsp.demo.model.user;

import br.ifsp.demo.model.movie.Grade;
import br.ifsp.demo.model.movie.MovieId;

public record PostRatingRequest(
        MovieId movieId,
        Grade grade
) {
}
