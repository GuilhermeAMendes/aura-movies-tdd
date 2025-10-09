package br.ifsp.demo.domain.user;

import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.MovieId;

public record PostRatingRequest(
        MovieId movieId,
        Grade grade
) {
}
