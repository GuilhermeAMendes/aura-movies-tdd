package br.ifsp.demo.domain.controller;

import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.service.delete.DeleteRateService;
import br.ifsp.demo.domain.service.get.GetRatedMoviesService;
import br.ifsp.demo.domain.service.patch.PatchRateService;
import br.ifsp.demo.domain.service.post.PostRateService;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/ratings")
@AllArgsConstructor
public class RatingsController {

    private final AuthenticationInfoService authenticationInfoService;
    private final GetRatedMoviesService getRatedMoviesService;
    private final PostRateService postRateService;
    private final PatchRateService patchRateService;
    private final DeleteRateService deleteRateService;

    @GetMapping
    public ResponseEntity<GetRatedMoviesService.RatedServiceResponseDTO> getRatedMoviesFromUser() {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        var request = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        var response = getRatedMoviesService.restoreRatedMovies(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<PostRateService.PostRateServiceResponseDTO> postRatedMovies(
            @RequestBody PostRateService.PostRateServiceRequestDTO postRatingRequest) {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        var request = new PostRateService.PostRateServiceRequestDTO(
                userId,
                postRatingRequest.movieId(),
                postRatingRequest.grade()
        );
        var response = postRateService.saveRate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{movieId}")
    public ResponseEntity<PatchRateService.PatchRateServiceResponseDTO> patchRatedMovies(
            @PathVariable UUID movieId,
            @RequestBody PatchRateService.PatchRateServiceRequestDTO patchRateRequest) {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();

        var request = new PatchRateService.PatchRateServiceRequestDTO(
                userId,
                new MovieId(movieId),
                patchRateRequest.grade()
        );
        var response = patchRateService.patchRate(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteRatedMovies(@PathVariable String movieId) {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        var request = new DeleteRateService.DeleteRateServiceRequestDTO(
                userId,
                new MovieId(movieId)
        );
        deleteRateService.deleteRate(request);
        return ResponseEntity.noContent().build();
    }
}
