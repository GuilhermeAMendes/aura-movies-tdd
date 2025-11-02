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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetRatedMoviesServiceImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private JpaMovieRepository jpaMovieRepository;

    @InjectMocks
    private GetRatedMoviesServiceImpl sut;

    private static List<Movie> createMockListAvailableMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Forrest Gump", Genre.DRAMA));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Parasite", Genre.THRILLER));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Pulp Fiction", Genre.COMEDY));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "The Lord of the Rings: The Fellowship of the Ring", Genre.FANTASY));
        return movies;
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-2]")
    @DisplayName("[SC-2.1] - Should return a list of rated movies for a user")
    void shouldReturnListOfRatedMoviesForAUser() {
        // Given
        UUID userId = UUID.randomUUID();
        Random grader = new Random();
        List<Movie> movies = createMockListAvailableMovies();
        List<Rating> userRatings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        movies.forEach(movie -> {
            int randomGrade = grader.nextInt(5) + 1;
            userRatings.add(new Rating(movie.getMovieId(), new Grade(randomGrade), now));
        });
        User user = User.builder().id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .ratings(userRatings)
                .password("secret").build();

        // When
        when(jpaUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
        movies.forEach(movie -> {
            when(jpaMovieRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));
        });
        GetRatedMoviesService.RatedServiceRequestDTO requestDTO = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        GetRatedMoviesService.RatedServiceResponseDTO responseDTO = sut.restoreRatedMovies(requestDTO);

        List<GetRatedMoviesService.RatedMovieDTO> result = responseDTO.ratedMovies();

        // Then
        assertThat(result).isNotNull().isNotEmpty().hasSize(movies.size());
        for (Movie movie : movies) {
            Rating rating = userRatings.stream()
                    .filter(r -> r.getMovieId().equals(movie.getMovieId()))
                    .findFirst()
                    .orElseThrow();
            GetRatedMoviesService.RatedMovieDTO ratedMovieDTO = result.stream()
                    .filter(rm -> rm.movieId().equals(movie.getMovieId()))
                    .findFirst()
                    .orElseThrow();
            assertThat(ratedMovieDTO.movieId()).isEqualTo(movie.getMovieId());
            assertThat(ratedMovieDTO.title()).isEqualTo(movie.getTitle());
            assertThat(ratedMovieDTO.genre()).isEqualTo(movie.getGenre());
            assertThat(ratedMovieDTO.grade()).isEqualTo(rating.getGrade());
            assertThat(ratedMovieDTO.lastGradedAt()).isEqualTo(rating.getLastGradedAt());
        }
        verify(jpaUserRepository, times(1)).findUserById(userId);
        movies.forEach(movie -> verify(jpaMovieRepository, times(1)).findById(movie.getMovieId()));
    }


    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-2]")
    @DisplayName("[SC-2.2] - Should return empty list of rated movies for user")
    void shouldReturnEmptyListOfRatedMoviesForAUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .ratings(List.of())
                .password("secret").build();
        // When
        when(jpaUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
        GetRatedMoviesService.RatedServiceRequestDTO requestDTO = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        GetRatedMoviesService.RatedServiceResponseDTO responseDTO = sut.restoreRatedMovies(requestDTO);
        List<GetRatedMoviesService.RatedMovieDTO> result = responseDTO.ratedMovies();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(jpaUserRepository, times(1)).findUserById(userId);
        verifyNoInteractions(jpaMovieRepository);
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-2]")
    @DisplayName("[SC-2.3] - Should throw when user not found")
    void shouldThrowWhenUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(jpaUserRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sut.restoreRatedMovies(new GetRatedMoviesService.RatedServiceRequestDTO(userId))).
                isInstanceOf(UserNotFoundException.class);

        verify(jpaUserRepository, times(1)).findUserById(userId);

    }
}