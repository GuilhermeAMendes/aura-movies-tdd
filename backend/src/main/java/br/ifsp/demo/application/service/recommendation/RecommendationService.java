package br.ifsp.demo.application.service.recommendation;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.domain.movie.repository.MovieRepository;
import br.ifsp.demo.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecommendationService {
    private final UserRepository userRepository;
    private final  MovieRepository movieRepository;

    public RecommendationService(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public List<Movie> recommendMovies(UUID userId){
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) return new ArrayList<>();

        List<Movie> moviesAvailable = movieRepository.findAll();

        return new ArrayList<>(
                moviesAvailable
        );
    }
}
