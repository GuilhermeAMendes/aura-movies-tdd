package br.ifsp.demo.service.post;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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
}
