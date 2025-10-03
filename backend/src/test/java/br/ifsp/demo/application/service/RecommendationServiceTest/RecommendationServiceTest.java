package br.ifsp.demo.application.service.RecommendationServiceTest;

import br.ifsp.demo.application.service.recommendation.RecommendationService;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.enums.Genre;
import br.ifsp.demo.domain.movie.repository.MovieRepository;
import br.ifsp.demo.domain.movie.valueObjects.MovieId;
import br.ifsp.demo.domain.movie.valueObjects.Note;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.domain.user.entity.Rating;
import br.ifsp.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    public static Stream<Arguments> recommendationMovies() {
        return Stream.of(
                Arguments.of(Genre.ACTION, "The Dark Knight", "Mad Max: Fury Road"),
                Arguments.of(Genre.SCI_FI, "Inception", "Blade Runner 2049"),
                Arguments.of(Genre.DRAMA, "The Godfather", "Forrest Gump")
        );
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @ParameterizedTest
    @MethodSource("recommendationMovies")
    @DisplayName("[SC-4.1] - Should return personalized movies for a client with ratings")
    void shouldReturnPersonalizedMovies(Genre genre, String ratedMovieTitle, String expectedMovieTitle) {
        //Given
        UUID userId = UUID.randomUUID();
        List<Movie> mockMovieList = createMockMovieList();
        Movie ratedMovie = mockMovieList.stream()
                .filter(m -> m.getTitle().equals(ratedMovieTitle))
                .findFirst().get();
        Movie expectedMovie = mockMovieList.stream()
                .filter(m -> m.getTitle().equals(expectedMovieTitle))
                .findFirst().get();
        Rating rating = new Rating(UUID.randomUUID(), ratedMovie.getId(), new Note(5), LocalDateTime.now());
        User user = new User(userId, "Lucas", List.of(rating));

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);
        List<Movie> result = recommendationService.recommendMovies(userId);

        // Then
        long countOfPreferredGenre = result.stream()
                .filter(movie -> movie.getGenre() == genre)
                .count();

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.size()).isEqualTo(MINIMAL_RECOMMENDATIONS);
        assertThat(countOfPreferredGenre).isGreaterThanOrEqualTo(result.size()/2);
        assertThat(result).contains(expectedMovie);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @DisplayName("[SC-4.2] - Should return 10 general movies for a user with no ratings")
    void shouldReturnGeneralMoviesForUserWithNoRatings() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Lucas", List.of());
        List<Movie> mockMovieList = createMockMovieList();

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
