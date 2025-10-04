package br.ifsp.demo.service.patch;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;

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

        if (movieRepository.findById(request.movieId()).isEmpty()) return null;

        Rating updatedRating = user.updateRating(request.movieId(), request.grade());

        userRepository.save(user);

        return new PatchRateServiceResponseDTO(updatedRating);
    }
}
