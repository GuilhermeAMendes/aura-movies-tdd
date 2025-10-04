package br.ifsp.demo.service.patch;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
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
        Rating baseRating = new Rating(UUID.randomUUID(), movie.getMovieId(), oldGrade, LocalDateTime.now());
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

}
