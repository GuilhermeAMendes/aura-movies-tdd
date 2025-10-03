package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.movie.Genre;
import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.repository.JpaUserRepository;
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
            userRatings.add(new Rating(UUID.randomUUID(), movie.getMovieId(), new Grade(randomGrade), LocalDateTime.now()));
        });
        User user = User.builder().id(userId)
                .name("Lucas")
                .lastname("Java")
                .email("lucasjava@gmail.com")
                .ratings(userRatings)
                .password("secret").build();

        // When
        when(jpaUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
        RatedServiceRequestDTO requestDTO = new GetRatedMoviesService.RatedServiceRequestDTO(userId);
        RatedServiceResponseDTO responseDTO = sut.restoreRatedMovies(requestDTO);

        List<Rating> result = responseDTO.ratings();

        // Then
        assertThat(result).isNotNull().isNotEmpty().hasSize(movies.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(userRatings);
        verify(jpaUserRepository, times(1)).findUserById(userId);
    }
}
