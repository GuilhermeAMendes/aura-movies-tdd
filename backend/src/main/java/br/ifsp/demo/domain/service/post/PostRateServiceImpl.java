package br.ifsp.demo.domain.service.post;

import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

@Service
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
