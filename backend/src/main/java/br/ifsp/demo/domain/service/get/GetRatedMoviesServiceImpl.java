package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetRatedMoviesServiceImpl implements GetRatedMoviesService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public GetRatedMoviesServiceImpl(final JpaUserRepository userRepository, final JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public RatedServiceResponseDTO restoreRatedMovies(RatedServiceRequestDTO request) {
        Optional<User> userOptional = userRepository.findUserById(request.userId());

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User restoredUser = userOptional.get();

        List<RatedMovieDTO> ratedMovies = restoredUser.getRatings().stream()
                .map(rating -> {
                    Movie movie = movieRepository.findById(rating.getMovieId())
                            .orElseThrow(() -> new MovieNotFoundException("Movie not found for rating"));
                    return new RatedMovieDTO(
                            movie.getMovieId(),
                            movie.getTitle(),
                            movie.getGenre(),
                            rating.getGrade(),
                            rating.getLastGradedAt()
                    );
                })
                .collect(Collectors.toList());

        return new RatedServiceResponseDTO(ratedMovies);
    }
}
