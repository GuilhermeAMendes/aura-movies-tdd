package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.domain.model.user.User;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetRecommendationServiceImpl implements GetRecommendationService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public GetRecommendationServiceImpl(JpaUserRepository userRepository, JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public RecommendationServiceResponseDTO recommendMovies(RecommendationServiceRequestDTO request) {
        Optional<User> user = userRepository.findById(request.userId());

        if (user.isEmpty()) throw new UserNotFoundException("User not found!");

        List<Movie> moviesAvailable = movieRepository.findAll();

        User foundUser = user.get();
        if (foundUser.getRatings() == null || foundUser.getRatings().isEmpty()) {
            return new RecommendationServiceResponseDTO(moviesAvailable);
        }

        // get movies that the user positively rated
        Set<UUID> positivelyRatedMovieIds = foundUser.getRatings().stream()
                .filter(r -> r.getGrade() != null && r.getGrade().value() >= 4)
                .map(r -> r.getMovieId().unwrap())
                .collect(Collectors.toSet());

        // find out user's preferred genre based on their ratings
        Set<Genre> preferredGenres = moviesAvailable.stream()
                .filter(m -> positivelyRatedMovieIds.contains(m.getMovieId().unwrap()))
                .map(Movie::getGenre)
                .collect(Collectors.toSet());

        // starting list of recommendations based on the positively rated movies and their genres
        List<Movie> recommendations = moviesAvailable.stream()
                .filter(movie -> preferredGenres.contains(movie.getGenre()))
                .toList();

        // avoid recommending movies the user already rated, add this:
        Set<UUID> alreadyRatedIds = foundUser.getRatings().stream()
                .map(r -> r.getMovieId().unwrap())
                .collect(Collectors.toSet());

        // final list of recommendations that remove already rated movies from the starting list
        List<Movie> finalRecommendations = recommendations.stream()
                .filter(movie -> !alreadyRatedIds.contains(movie.getMovieId().unwrap()))
                .toList();

        return new RecommendationServiceResponseDTO(finalRecommendations);
    }
}
