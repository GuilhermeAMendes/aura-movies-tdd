package br.ifsp.demo.service.get;

import br.ifsp.demo.model.user.Rating;

import java.util.List;
import java.util.UUID;

public interface GetRatedMoviesService {
    RatedServiceResponseDTO restoreRatedMovies(RatedServiceRequestDTO request);

    record RatedServiceRequestDTO(
            UUID userId
    ) {
    }

    record RatedServiceResponseDTO(List<Rating> ratings) {}
}
