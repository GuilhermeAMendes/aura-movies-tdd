package br.ifsp.demo.service.get;

import br.ifsp.demo.model.movie.Genre;
import br.ifsp.demo.model.movie.Movie;
import br.ifsp.demo.model.movie.MovieId;
import br.ifsp.demo.model.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllMoviesServiceImplTest {

    @Mock
    private JpaMovieRepository movieRepository;

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private GetAllMoviesServiceImpl sut;

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return all movies when user is found")
    void shouldReturnAllMoviesWhenUserIsFound() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .build();

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Movie 1", Genre.ACTION));
        movies.add(new Movie(new MovieId(UUID.randomUUID()), "Movie 2", Genre.COMEDY));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findAll()).thenReturn(movies);

        GetAllMoviesService.GetAllMoviesRequestDTO request = new GetAllMoviesService.GetAllMoviesRequestDTO(userId);
        GetAllMoviesService.GetAllMoviesResponseDTO response = sut.getAllMovies(request);

        assertThat(response.movies()).isNotNull();
        assertThat(response.movies()).hasSize(2);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        GetAllMoviesService.GetAllMoviesRequestDTO request = new GetAllMoviesService.GetAllMoviesRequestDTO(userId);

        assertThatThrownBy(() -> sut.getAllMovies(request))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, never()).findAll();
    }
}
