package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.exception.MovieNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMovieByIdServiceImpl implements GetMovieByIdService {

    private final JpaMovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public GetMovieByIdResponseDTO getMovieById(GetMovieByIdRequestDTO request) {

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        return new GetMovieByIdResponseDTO(movie);
    }
}