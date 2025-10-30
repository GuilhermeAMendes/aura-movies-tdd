package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetAllMoviesServiceImpl implements GetAllMoviesService {

    private final JpaMovieRepository movieRepository;
    private final JpaUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public GetAllMoviesResponseDTO getAllMovies(GetAllMoviesRequestDTO request) {

        Optional<User> user = userRepository.findById(request.userId());

        if (user.isEmpty()) throw new UserNotFoundException("User not found!");

        List<Movie> movies = movieRepository.findAll();

        return new GetAllMoviesResponseDTO(movies);
    }
}
