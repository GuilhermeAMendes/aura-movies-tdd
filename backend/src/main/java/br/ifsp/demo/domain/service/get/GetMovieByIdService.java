package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;

import java.util.UUID;

public interface GetMovieByIdService {

    GetMovieByIdResponseDTO getMovieById(GetMovieByIdRequestDTO request);

    record GetMovieByIdRequestDTO(
            UUID userId,
            MovieId movieId
    ) {
    }

    record GetMovieByIdResponseDTO(
            Movie movie,
            Rating rating
    ) {
    }
}