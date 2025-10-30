package br.ifsp.demo.controller;

import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.service.get.GetMovieByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final GetMovieByIdService getMovieByIdService;

    @GetMapping("/{id}")
    public ResponseEntity<GetMovieByIdService.GetMovieByIdResponseDTO> getMovieById(@PathVariable String id) {
        MovieId movieId = new MovieId(id);

        var request = new GetMovieByIdService.GetMovieByIdRequestDTO(movieId);
        var response = getMovieByIdService.getMovieById(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
