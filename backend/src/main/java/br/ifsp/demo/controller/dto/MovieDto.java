package br.ifsp.demo.controller.dto;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Movie;

public record MovieDto(
        MovieIdDto movieId,
        String title,
        Genre genre
) {

    public MovieDto(Movie movie) {
        this(
                new MovieIdDto(movie.getMovieId().getId().toString()),
                movie.getTitle(),
                movie.getGenre()
        );
    }
}