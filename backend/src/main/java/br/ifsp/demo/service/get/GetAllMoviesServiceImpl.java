package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetAllMoviesServiceImpl implements GetAllMoviesService {

    private final JpaMovieRepository movieRepository;
    private final JpaUserRepository userRepository;


    @Override
    public GetAllMoviesResponseDTO getAllMovies(GetAllMoviesRequestDTO request) {

        Optional<User> user = userRepository.findById(request.userId());

        if (user.isEmpty()) throw new UserNotFoundException("User not found!");

        List<Movie> movies = movieRepository.findAll();

        return new GetAllMoviesResponseDTO(movies);
    }
}
