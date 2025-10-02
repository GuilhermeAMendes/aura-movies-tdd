package br.ifsp.demo.application.service.RecommendationServiceTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {
    private static final int MINIMAL_RECOMMENDATIONS = 10;

    @Mock
    private UserRepository UserRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private static List<Movie> createMockMovieList(){
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < MINIMAL_RECOMMENDATIONS; i++){
            movies.add(new Movie());
        }
        return movies;
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @DisplayName("[SC-4.2] - Should return 10 general movies for a user with no ratings")
    void shouldReturnGeneralMoviesForUserWithNoRatings() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Lucas", List.of());
        List<Movie> mockMovieList  = createMockMovieList();

        // When
        when(UserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);
        List<Movie> result = recommendationService.recommendMovies(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(MINIMAL_RECOMMENDATIONS);
        verify(UserRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }
}
