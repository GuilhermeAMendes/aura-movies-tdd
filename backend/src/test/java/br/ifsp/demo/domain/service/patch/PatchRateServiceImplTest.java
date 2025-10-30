package br.ifsp.demo.domain.service.patch;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.user.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.ReviewNotFoundException;
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
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatchRateServiceImplTest {
    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaMovieRepository movieRepository;

    @InjectMocks
    private PatchRateServiceImpl sut;

    public static Stream<Arguments> createMovie() {
        return Stream.of(
                Arguments.of(
                        (new Movie(new MovieId(UUID.randomUUID()), "Forrest Gump", Genre.DRAMA))
                ),
                Arguments.of(
                        (new Movie(new MovieId(UUID.randomUUID()), "Parasite", Genre.THRILLER))
                ),
                Arguments.of(
                        (new Movie(new MovieId(UUID.randomUUID()), "Pulp Fiction", Genre.COMEDY))
                ),
                Arguments.of(
                        (new Movie(new MovieId(UUID.randomUUID()), "The Lord of the Rings: The Fellowship of the Ring", Genre.FANTASY))
                )
        );
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-3]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-3.1] - Should update an existing rating for a movie")
    void shouldUpdateRatingForAMovie(Movie movie) {
        // Given
        UUID userId = UUID.randomUUID();
        Grade oldGrade = new Grade(3);
        Grade newGrade = new Grade(4);
        Rating baseRating = new Rating(movie.getMovieId(), oldGrade, LocalDateTime.now());
        List<Rating> userRatings = new ArrayList<>();
        userRatings.add(baseRating);

        User user = User.builder().id(userId).ratings(userRatings).build();

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));
        PatchRateService.PatchRateServiceRequestDTO request = new PatchRateService.PatchRateServiceRequestDTO(userId, movie.getMovieId(), newGrade);
        PatchRateService.PatchRateServiceResponseDTO result = sut.patchRate(request);

        // Then
        assertThat(result.rating()).isNotNull();
        assertThat(result.rating().getGrade()).isEqualTo(newGrade);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findById(movie.getMovieId());
        verify(userRepository, times(1)).save(user);
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-3]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-3.3] - Should reject update on missing review")
    void shouldRejectUpdateOnMissingReview(Movie movie) {
        // Given
        UUID userId = UUID.randomUUID();
        Grade newGrade = new Grade(4);
        List<Rating> userRatings = new ArrayList<>();

        User user = User.builder().id(userId).ratings(userRatings).build();

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));
        PatchRateService.PatchRateServiceRequestDTO request = new PatchRateService.PatchRateServiceRequestDTO(userId, movie.getMovieId(), newGrade);

        // Then
        assertThatThrownBy(() -> sut.patchRate(request))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-3]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-3.4] - Should reject update on missing movie")
    void shouldRejectUpdateOnMissingMovie(Movie movie) {
        // Given
        UUID userId = UUID.randomUUID();
        Grade newGrade = new Grade(4);

        User user = User.builder().id(userId).ratings(new ArrayList<>()).build();

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.empty());
        PatchRateService.PatchRateServiceRequestDTO request = new PatchRateService.PatchRateServiceRequestDTO(userId, movie.getMovieId(), newGrade);

        // Then
        assertThatThrownBy(() -> sut.patchRate(request)).isInstanceOf(MovieNotFoundException.class);
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-3]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-3.5] - Should reject review alteration by non-authenticated user")
    void shouldRejectUnauthorisedUser(Movie movie) {
        UUID userId = UUID.randomUUID();
        Grade newGrade = new Grade(4);
        PatchRateService.PatchRateServiceRequestDTO request = new PatchRateService.PatchRateServiceRequestDTO(userId, movie.getMovieId(), newGrade);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.patchRate(request)).isInstanceOf(UserNotFoundException.class);
    }

}
