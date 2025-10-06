package br.ifsp.demo.service.delete;

import br.ifsp.demo.domain.movie.MovieId;
import java.util.UUID;

public interface DeleteRateService {
    void deleteRate(DeleteRateServiceRequestDTO request);

    record DeleteRateServiceRequestDTO(
            UUID userId,
            MovieId movieId
    ) {}

    record DeleteRateServiceResponseDTO() {}
}
