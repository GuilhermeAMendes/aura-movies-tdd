package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;

public interface GetMovieByIdService {

    GetMovieByIdResponseDTO getMovieById(GetMovieByIdRequestDTO request);

    record GetMovieByIdRequestDTO(
            MovieId movieId
    ) {
    }

    record GetMovieByIdResponseDTO(
            Movie movie
    ) {
    }
}