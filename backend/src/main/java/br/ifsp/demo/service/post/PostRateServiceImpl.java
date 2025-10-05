package br.ifsp.demo.service.post;

import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.MovieNotFoundException;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;

public class PostRateServiceImpl implements PostRateService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public PostRateServiceImpl(JpaUserRepository userRepository, JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public PostRateServiceResponseDTO saveRate(PostRateServiceRequestDTO request) {
        User user = userRepository.findById(request.userId()).orElseThrow(() ->
                new UserNotFoundException("User not found")
        );

        if (movieRepository.findById(request.movieId()).isEmpty()) throw new MovieNotFoundException("Movie not found");

        Rating rating = user.addRating(request.movieId(), request.grade());
        userRepository.save(user);

        return new PostRateServiceResponseDTO(rating);
    }
}
