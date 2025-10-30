package br.ifsp.demo.service.get;

import br.ifsp.demo.model.movie.Movie;

import java.util.List;
import java.util.UUID;

public interface GetAllMoviesService {

    GetAllMoviesResponseDTO getAllMovies(GetAllMoviesRequestDTO request);

    record GetAllMoviesRequestDTO(
            UUID userId
    ) { }

    record GetAllMoviesResponseDTO(
            List<Movie> movies
    ) { }
}
