package br.ifsp.demo.controller;

import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.service.get.GetAllMoviesService;
import br.ifsp.demo.service.get.GetMovieByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final AuthenticationInfoService authenticationInfoService;
    private final GetMovieByIdService getMovieByIdService;
    private final GetAllMoviesService getAllMoviesService;

    @GetMapping("/{id}")
    public ResponseEntity<GetMovieByIdService.GetMovieByIdResponseDTO> getMovieById(@PathVariable String id) {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        MovieId movieId = new MovieId(id);

        var request = new GetMovieByIdService.GetMovieByIdRequestDTO(userId, movieId);
        var response = getMovieByIdService.getMovieById(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllMoviesService.GetAllMoviesResponseDTO> getAllMovies() {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();

        var request = new GetAllMoviesService.GetAllMoviesRequestDTO(userId);
        var response = getAllMoviesService.getAllMovies(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
