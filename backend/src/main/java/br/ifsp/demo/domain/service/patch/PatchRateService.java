package br.ifsp.demo.domain.service.patch;

import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;

import java.util.UUID;

public interface PatchRateService {
    PatchRateServiceResponseDTO patchRate(PatchRateServiceRequestDTO request);

    record PatchRateServiceRequestDTO(  UUID userId,
                                        MovieId movieId,
                                        Grade grade){}

    record PatchRateServiceResponseDTO(Rating rating){}
}

