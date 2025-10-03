package br.ifsp.demo.application.service.RecommendationServiceTest;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import br.ifsp.demo.service.get.GetRecommendationService;
import br.ifsp.demo.service.get.GetRecommendationServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

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

        Rating rating = new Rating(UUID.randomUUID(), ratedMovie.getMovieId(), new Grade(5), LocalDateTime.now());
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
}
