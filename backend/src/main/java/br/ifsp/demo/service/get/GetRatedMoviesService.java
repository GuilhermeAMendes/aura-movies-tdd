package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.user.Rating;

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
