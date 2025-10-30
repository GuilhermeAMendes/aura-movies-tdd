package br.ifsp.demo.domain.service.post;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.domain.model.user.User;
import br.ifsp.demo.domain.exception.MovieNotFoundException;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.domain.service.post.PostRateService;
import br.ifsp.demo.domain.service.post.PostRateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostRateServiceImplTest {

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaMovieRepository movieRepository;

    @InjectMocks
    private PostRateServiceImpl sut;

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-1]")
    @DisplayName("[SC-1.1] - Should add a new rating to the user's profile")
    void shouldAddNewRatingToUserProfile() {
        // Given
        UUID userId = UUID.randomUUID();
        Random random = new Random();
        Movie movieToRate = new Movie(new MovieId(UUID.randomUUID()), "Inception", Genre.SCI_FI);
        Grade grade = new Grade(random.nextInt(5));

        User user = User.builder().id(userId).ratings(new ArrayList<>()).build();


        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieToRate.getMovieId())).thenReturn(Optional.of(movieToRate));
        PostRateService.PostRateServiceRequestDTO request =
                new PostRateService.PostRateServiceRequestDTO(userId, movieToRate.getMovieId(), grade);
        PostRateService.PostRateServiceResponseDTO result = sut.saveRate(request);

        // Then
        assertThat(result.rating()).isNotNull();
        assertThat(result.rating().getMovieId()).isEqualTo(movieToRate.getMovieId());
        assertThat(result.rating().getGrade()).isEqualTo(grade);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findById(movieToRate.getMovieId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-1]")
    @DisplayName("[SC-1.3] - Should return movie not found")
    void shouldReturnMovieNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        Random random = new Random();
        Movie movieToRate = new Movie(new MovieId(UUID.randomUUID()), "Blade Runner: 2069", Genre.SCI_FI);
        Grade grade = new Grade(random.nextInt(5));

        User user = User.builder().id(userId).ratings(new ArrayList<>()).build();

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieToRate.getMovieId())).thenReturn(Optional.empty());
        PostRateService.PostRateServiceRequestDTO request =
                new PostRateService.PostRateServiceRequestDTO(userId, movieToRate.getMovieId(), grade);

        // Then
        assertThatThrownBy(() -> sut.saveRate(request)).isInstanceOf(MovieNotFoundException.class);
        verify(userRepository, times(1)).findById(userId);
        verify(movieRepository, times(1)).findById(movieToRate.getMovieId());
        verify(userRepository, never()).save(user);
    }

    @Test
    @Tag("UnitTest")
    @Tag("[US-1]")
    @DisplayName("[SC-1.4] - Should throw when user not found")
    void shouldThrowWhenUserNotFound() {
        // Given
        UUID missingUserId = UUID.randomUUID();
        MovieId movieId = new MovieId(UUID.randomUUID());
        Grade grade = new Grade(1);

        // When
        when(userRepository.findById(missingUserId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sut.saveRate(new PostRateService.PostRateServiceRequestDTO(missingUserId, movieId, grade)))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(missingUserId);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(movieRepository);
    }
}
