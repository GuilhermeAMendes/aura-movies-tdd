package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetRatedMoviesService {
    RatedServiceResponseDTO restoreRatedMovies(RatedServiceRequestDTO request);

    record RatedServiceRequestDTO(
            UUID userId
    ) {
    }

    record RatedServiceResponseDTO(List<RatedMovieDTO> ratedMovies) {}

    record RatedMovieDTO(
            MovieId movieId,
            String title,
            Genre genre,
            Grade grade,
            LocalDateTime lastGradedAt
    ) {}
}
