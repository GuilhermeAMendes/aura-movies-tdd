package br.ifsp.demo.domain.service.delete;

import br.ifsp.demo.domain.model.movie.MovieId;
import java.util.UUID;

public interface DeleteRateService {
    void deleteRate(DeleteRateServiceRequestDTO request);

    record DeleteRateServiceRequestDTO(
            UUID userId,
            MovieId movieId
    ) {}
}
