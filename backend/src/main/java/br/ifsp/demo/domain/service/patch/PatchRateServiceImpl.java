package br.ifsp.demo.domain.service.patch;

import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.ReviewNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

@Service
public class PatchRateServiceImpl implements PatchRateService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public PatchRateServiceImpl(JpaUserRepository userRepository, JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public PatchRateServiceResponseDTO patchRate(PatchRateServiceRequestDTO request) {
        User user = userRepository.findById(request.userId()).orElseThrow(() ->
                new UserNotFoundException("User not found")
                );

        if (movieRepository.findById(request.movieId()).isEmpty()) throw new MovieNotFoundException("Movie not found");

        if (user.getRatings().isEmpty()) throw new ReviewNotFoundException("Review not found");

        Rating updatedRating = user.updateRating(request.movieId(), request.grade());

        userRepository.save(user);

        return new PatchRateServiceResponseDTO(updatedRating);
    }
}
