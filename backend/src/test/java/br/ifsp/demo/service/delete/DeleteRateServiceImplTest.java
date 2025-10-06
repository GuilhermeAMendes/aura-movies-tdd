package br.ifsp.demo.service.delete;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.MovieNotFoundException;
import br.ifsp.demo.exception.ReviewNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteRateServiceImplTest {

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaMovieRepository movieRepository;

    @InjectMocks
    private DeleteRateServiceImpl sut;

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
    @Tag("[US-5]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-5.1] - Should delete an existing rating for a movie")
    void ShouldDeleteMovieReview(Movie movie) {
        //Given
        UUID userId = UUID.randomUUID();
        Grade grade = new Grade(3);
        Rating rating = new Rating(movie.getMovieId(), grade, LocalDateTime.now());
        List<Rating> userRatings = new ArrayList<>();
        userRatings.add(rating);

        User user = User.builder().id(userId).ratings(userRatings).build();

        //When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));
        DeleteRateService.DeleteRateServiceRequestDTO request = new DeleteRateService.DeleteRateServiceRequestDTO(userId, movie.getMovieId());
        sut.deleteRate(request);

        //Then
        assertThat(user.getRatings()).isEmpty();
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-5]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-5.2] - Should reject deletion of missing review")
    void ShouldRejectDeleteMissingReview(Movie movie) {
        //Given
        UUID userId = UUID.randomUUID();
        List<Rating> userRatings = new ArrayList<>();

        User user = User.builder().id(userId).ratings(userRatings).build();

        //When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));
        DeleteRateService.DeleteRateServiceRequestDTO request = new DeleteRateService.DeleteRateServiceRequestDTO(userId, movie.getMovieId());

        //Then
        assertThatThrownBy(() -> sut.deleteRate(request)).isInstanceOf(ReviewNotFoundException.class);
    }

    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-5]")
    @ParameterizedTest
    @MethodSource("createMovie")
    @DisplayName("[SC-5.3] - Should reject review deletion for missing movie")
    void ShouldRejectDeleteMissingMovie(Movie movie) {
        //Given
        UUID userId = UUID.randomUUID();

        List<Rating> userRatings = new ArrayList<>();

        User user = User.builder().id(userId).ratings(userRatings).build();

        //Whwn
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movie.getMovieId())).thenReturn(Optional.empty());
        DeleteRateService.DeleteRateServiceRequestDTO request = new DeleteRateService.DeleteRateServiceRequestDTO(userId, movie.getMovieId());


        //Then
        assertThatThrownBy(() -> sut.deleteRate(request)).isInstanceOf(MovieNotFoundException.class);
    }
}
