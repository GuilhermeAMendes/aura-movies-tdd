package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.user.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.domain.service.get.GetMovieByIdService;
import br.ifsp.demo.domain.service.get.GetMovieByIdServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMovieByIdServiceImplTest {

    @Mock
    private JpaMovieRepository movieRepository;

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private GetMovieByIdServiceImpl sut;

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should return movie when user and movie are found")
    void shouldReturnMovieWhenUserAndMovieFound() {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Movie movie = new Movie(movieId, "Test Movie", Genre.ACTION);

        User user = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        GetMovieByIdService.GetMovieByIdRequestDTO request = new GetMovieByIdService.GetMovieByIdRequestDTO(userId, movieId);
        GetMovieByIdService.GetMovieByIdResponseDTO response = sut.getMovieById(request);

        assertThat(response.movie()).isNotNull();
        assertThat(response.movie()).isEqualTo(movie);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        GetMovieByIdService.GetMovieByIdRequestDTO request = new GetMovieByIdService.GetMovieByIdRequestDTO(userId, movieId);

        assertThatThrownBy(() -> sut.getMovieById(request))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, never()).findById(any());
    }

    @Test
    @Tag("UnitTest")
    @Tag("Structural")
    @DisplayName("Should throw exception when movie is not found")
    void shouldThrowExceptionWhenMovieNotFound() {
        UUID userId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());

        User user = User.builder()
                .id(userId)
                .name("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        GetMovieByIdService.GetMovieByIdRequestDTO request = new GetMovieByIdService.GetMovieByIdRequestDTO(userId, movieId);

        assertThatThrownBy(() -> sut.getMovieById(request))
                .isInstanceOf(MovieNotFoundException.class);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findById(movieId);
    }
}
