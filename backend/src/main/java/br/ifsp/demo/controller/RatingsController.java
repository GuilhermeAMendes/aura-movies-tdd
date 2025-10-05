package br.ifsp.demo.controller;

import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.service.get.GetRatedMoviesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/ratings")
@AllArgsConstructor
public class RatingsController {

    private final AuthenticationInfoService authenticationInfoService;
    private final GetRatedMoviesService getRatedMoviesService;

    @GetMapping
    public ResponseEntity<GetRatedMoviesService.RatedServiceResponseDTO> getRatedMoviesFromUser() {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        var request = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        var response = getRatedMoviesService.restoreRatedMovies(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
