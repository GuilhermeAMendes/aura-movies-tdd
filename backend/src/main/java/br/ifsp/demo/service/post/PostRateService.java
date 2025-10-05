package br.ifsp.demo.service.post;

import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Rating;

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
