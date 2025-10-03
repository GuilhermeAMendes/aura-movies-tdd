package br.ifsp.demo.service;

import br.ifsp.demo.domain.movie.Movie;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<Movie> recommendMovies(RecommendationServiceRequestDTO request);

    record RecommendationServiceRequestDTO(
            UUID userId
    ) {
    }

    record RecommendationServiceResponseDTO(
            List<Movie> recommendations
    ) {
    }
}
