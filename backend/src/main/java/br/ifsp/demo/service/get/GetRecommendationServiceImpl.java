package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
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

        Set<UUID> positivelyRatedMovieIds = foundUser.getRatings().stream()
                .filter(r -> r.getGrade() != null && r.getGrade().value() >= 4)
                .map(r -> r.getMovieId().unwrap())
                .collect(Collectors.toSet());

        Set<Genre> preferredGenres = moviesAvailable.stream()
                .filter(m -> positivelyRatedMovieIds.contains(m.getMovieId().unwrap()))
                .map(Movie::getGenre)
                .collect(Collectors.toSet());

        List<Movie> prioritized = moviesAvailable.stream()
                .sorted((m1, m2) -> {
                    boolean m1Preferred = preferredGenres.contains(m1.getGenre());
                    boolean m2Preferred = preferredGenres.contains(m2.getGenre());
                    if (m1Preferred == m2Preferred) return 0;
                    return m1Preferred ? -1 : 1;
                })
                .toList();

        return new RecommendationServiceResponseDTO(prioritized);
    }
}
