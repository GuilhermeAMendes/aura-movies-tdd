package br.ifsp.demo.domain.service.post;

import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.user.Rating;

import java.util.UUID;

public interface PostRateService {
    PostRateServiceResponseDTO saveRate(PostRateServiceRequestDTO request);

    record PostRateServiceRequestDTO(
            UUID userId,
            MovieId movieId,
            Grade grade
    ) {
    }

    record PostRateServiceResponseDTO(Rating rating) {
    }
}
