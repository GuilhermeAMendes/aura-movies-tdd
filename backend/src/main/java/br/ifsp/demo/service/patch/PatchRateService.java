package br.ifsp.demo.service.patch;

import br.ifsp.demo.model.movie.Grade;
import br.ifsp.demo.model.movie.MovieId;
import br.ifsp.demo.model.user.Rating;

import java.util.UUID;

public interface PatchRateService {
    PatchRateServiceResponseDTO patchRate(PatchRateServiceRequestDTO request);

    record PatchRateServiceRequestDTO(  UUID userId,
                                        MovieId movieId,
                                        Grade grade){}

    record PatchRateServiceResponseDTO(Rating rating){}
}

