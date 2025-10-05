package br.ifsp.demo.controller;

import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.service.get.GetRecommendationService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/recommendations")
@AllArgsConstructor
public class RecommendationsController {

    private final AuthenticationInfoService authenticationInfoService;
    private final GetRecommendationService getRecommendationService;

    @GetMapping
    public ResponseEntity<GetRecommendationService.RecommendationServiceResponseDTO> getRecommendations() {
        UUID userId = authenticationInfoService.getAuthenticatedUserId();
        GetRecommendationService.RecommendationServiceResponseDTO response = getRecommendationService.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}


