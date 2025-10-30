package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.MovieNotFoundException;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
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

        Optional<User> user = userRepository.findById(request.userId());

        if (user.isEmpty()) throw new UserNotFoundException("User not found!");

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        return new GetMovieByIdResponseDTO(movie);
    }
}