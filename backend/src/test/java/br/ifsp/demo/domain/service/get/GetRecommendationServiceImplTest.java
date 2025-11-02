package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.rating.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetRecommendationServiceImplTest {

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaMovieRepository movieRepository;

    @InjectMocks
    private GetRecommendationServiceImpl sut;

    private static List<Movie> createMockMovieList() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Dark Knight", Genre.ACTION));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Mad Max: Fury Road", Genre.ACTION));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Inception", Genre.SCI_FI));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Blade Runner 2049", Genre.SCI_FI));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Godfather", Genre.DRAMA));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Forrest Gump", Genre.DRAMA));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Parasite", Genre.THRILLER));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Silence of the Lambs", Genre.THRILLER));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Pulp Fiction", Genre.COMEDY));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Lord of the Rings: The Fellowship of the Ring", Genre.FANTASY));
        return movies;
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
    @Tag("[US-4]")
    @ParameterizedTest
    @MethodSource("recommendationMovies")
    @DisplayName("[SC-4.1] - Should return personalized movies for a client with ratings")
    void shouldReturnPersonalizedMovies(Genre preferredGenre, String ratedMovieTitle, String expectedMovieTitle) {
        UUID userId = UUID.randomUUID();
        List<Movie> mockMovieList = createMockMovieList();
        Movie ratedMovie = mockMovieList.stream()
                .filter(m -> m.getTitle().equals(ratedMovieTitle))
                .findFirst().orElseThrow();
        Movie expectedMovie = mockMovieList.stream()
                .filter(m -> m.getTitle().equals(expectedMovieTitle))
                .findFirst().orElseThrow();

        Rating rating = new Rating(ratedMovie.getMovieId(), new Grade(5), LocalDateTime.now());
        User user = User.builder()
                .id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .password("secret")
                .ratings(List.of(rating))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);

        List<Movie> result = sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId)).recommendations();

        long countOfPreferredGenre = result.stream()
                .filter(movie -> movie.getGenre() == preferredGenre)
                .count();

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(countOfPreferredGenre).isGreaterThanOrEqualTo(result.size() / 2);
        assertThat(result).contains(expectedMovie);
        assertThat(result).doesNotContain(ratedMovie);

        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-4]")
    @DisplayName("[SC-4.2] - Should return general movies for a user with no ratings")
    void shouldReturnGeneralMoviesForUserWithNoRatings() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .password("secret")
                .ratings(List.of())
                .build();
        List<Movie> mockMovieList = createMockMovieList();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);

        List<Movie> result = sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId)).recommendations();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-4]")
    @DisplayName("[SC-4.3] - Should throw when user not found")
    void shouldThrowWhenUserNotFound() {
        UUID missingUserId = UUID.randomUUID();

        when(userRepository.findById(missingUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(missingUserId)))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(missingUserId);
        verifyNoInteractions(movieRepository);
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return all movies when user ratings is null")
    void shouldReturnAllMoviesWhenUserRatingsIsNull() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .password("secret")
                .ratings(null)
                .build();
        List<Movie> mockMovieList = createMockMovieList();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);

        List<Movie> result = sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId)).recommendations();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(mockMovieList.size());
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should filter movies with grade less than 4")
    void shouldFilterMoviesWithGradeLessThan4() {
        UUID userId = UUID.randomUUID();
        List<Movie> mockMovieList = createMockMovieList();
        Movie ratedMovie = mockMovieList.getFirst();

        Rating lowRating = new Rating(ratedMovie.getMovieId(), new Grade(3), LocalDateTime.now());
        User user = User.builder()
                .id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .password("secret")
                .ratings(List.of(lowRating))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);

        List<Movie> result = sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId)).recommendations();

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should handle rating with null grade")
    void shouldHandleRatingWithNullGrade() {
        UUID userId = UUID.randomUUID();
        List<Movie> mockMovieList = createMockMovieList();
        Movie ratedMovie = mockMovieList.getFirst();

        Rating ratingWithNullGrade = new Rating(ratedMovie.getMovieId(), null, LocalDateTime.now());
        User user = User.builder()
                .id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .password("secret")
                .ratings(List.of(ratingWithNullGrade))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(mockMovieList);

        List<Movie> result = sut.recommendMovies(new GetRecommendationService.RecommendationServiceRequestDTO(userId)).recommendations();

        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }
}
