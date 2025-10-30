package br.ifsp.demo.domain.model.user;

import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;

public record PostRatingRequest(
        MovieId movieId,
        Grade grade
) {
}
