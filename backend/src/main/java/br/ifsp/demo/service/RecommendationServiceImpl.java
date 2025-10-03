package br.ifsp.demo.service;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public RecommendationServiceImpl(JpaUserRepository userRepository, JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> recommendMovies(RecommendationServiceRequestDTO request) {
        Optional<User> user = userRepository.findById(request.userId());

        if (user.isEmpty()) throw new UserNotFoundException("User not found!");

        List<Movie> moviesAvailable = movieRepository.findAll();

        return new ArrayList<>(
                moviesAvailable
        );
    }
}
