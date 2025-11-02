package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetMovieByIdServiceImpl implements GetMovieByIdService {

    private final JpaMovieRepository movieRepository;
    private final JpaUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public GetMovieByIdResponseDTO getMovieById(GetMovieByIdRequestDTO request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        Rating userRating = user.getRatings().stream()
                    .filter(rating -> rating.getMovieId().equals(request.movieId()))
                    .findFirst()
                    .orElse(null);

        return new GetMovieByIdResponseDTO(movie, userRating);
    }
}