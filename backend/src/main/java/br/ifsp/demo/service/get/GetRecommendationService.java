package br.ifsp.demo.service.get;

import br.ifsp.demo.model.movie.Movie;

import java.util.List;
import java.util.UUID;

public interface GetRecommendationService {
    RecommendationServiceResponseDTO recommendMovies(RecommendationServiceRequestDTO request);

    record RecommendationServiceRequestDTO(
            UUID userId
    ) {
    }

    record RecommendationServiceResponseDTO(
            List<Movie> recommendations
    ) {
    }
}
