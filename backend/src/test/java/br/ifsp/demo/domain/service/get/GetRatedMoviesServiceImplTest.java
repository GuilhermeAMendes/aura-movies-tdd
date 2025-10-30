package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.user.Rating;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.domain.exception.UserNotFoundException;
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
    private  JpaUserRepository jpaUserRepository;

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
        movies.forEach(movie -> {
            int randomGrade = grader.nextInt(5) + 1;
            userRatings.add(new Rating(movie.getMovieId(), new Grade(randomGrade), LocalDateTime.now()));
        });
        User user = User.builder().id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .ratings(userRatings)
                .password("secret").build();

        // When
        when(jpaUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
        GetRatedMoviesService.RatedServiceRequestDTO requestDTO = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        GetRatedMoviesService.RatedServiceResponseDTO responseDTO = sut.restoreRatedMovies(requestDTO);

        List<Rating> result = responseDTO.ratings();

        // Then
        assertThat(result).isNotNull().isNotEmpty().hasSize(movies.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(userRatings);
        verify(jpaUserRepository, times(1)).findUserById(userId);
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
        List<Rating> result = responseDTO.ratings();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(jpaUserRepository, times(1)).findUserById(userId);
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