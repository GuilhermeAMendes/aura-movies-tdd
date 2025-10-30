package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;

import java.util.UUID;

public interface GetMovieByIdService {

    GetMovieByIdResponseDTO getMovieById(GetMovieByIdRequestDTO request);

    record GetMovieByIdRequestDTO(
            UUID userId,
            MovieId movieId
    ) {
    }

    record GetMovieByIdResponseDTO(
            Movie movie
    ) {
    }
}