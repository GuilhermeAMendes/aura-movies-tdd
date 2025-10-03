package br.ifsp.demo.application.service.RecommendationServiceTest;

import br.ifsp.demo.application.service.recommendation.RecommendationService;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.enums.Genre;
import br.ifsp.demo.domain.movie.valueObjects.MovieId;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.domain.movie.repository.MovieRepository;
import br.ifsp.demo.domain.user.entity.Rating;
import br.ifsp.demo.domain.user.repository.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private static List<Movie> createMockMovieList() {
        return List.of(
                new Movie(new MovieId(UUID.randomUUID()), "The Dark Knight", Genre.ACTION),
                new Movie(new MovieId(UUID.randomUUID()), "Mad Max: Fury Road", Genre.ACTION),
                new Movie(new MovieId(UUID.randomUUID()), "Inception", Genre.SCI_FI),
                new Movie(new MovieId(UUID.randomUUID()), "Blade Runner 2049", Genre.SCI_FI),
                new Movie(new MovieId(UUID.randomUUID()), "The Godfather", Genre.DRAMA),
                new Movie(new MovieId(UUID.randomUUID()), "Forrest Gump", Genre.DRAMA),
                new Movie(new MovieId(UUID.randomUUID()), "Parasite", Genre.THRILLER),
                new Movie(new MovieId(UUID.randomUUID()), "The Silence of the Lambs", Genre.THRILLER),
                new Movie(new MovieId(UUID.randomUUID()), "Pulp Fiction", Genre.COMEDY),
                new Movie(new MovieId(UUID.randomUUID()), "The Lord of the Rings: The Fellowship of the Ring", Genre.FANTASY)
        );
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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);
        List<Movie> result = recommendationService.recommendMovies(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(MINIMAL_RECOMMENDATIONS);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }
}
