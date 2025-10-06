package br.ifsp.demo.service.delete;

import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.MovieNotFoundException;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaMovieRepository;
import br.ifsp.demo.repository.JpaUserRepository;

public class DeleteRateServiceImpl implements DeleteRateService {
    private final JpaUserRepository userRepository;
    private final JpaMovieRepository movieRepository;

    public DeleteRateServiceImpl(JpaUserRepository userRepository, JpaMovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }


    @Override
    public void deleteRate(DeleteRateServiceRequestDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (movieRepository.findById(request.movieId()).isEmpty()) {
            throw new MovieNotFoundException("Movie not found");
        }

        user.deleteRating(request.movieId());

        userRepository.save(user);

        new DeleteRateServiceResponseDTO();
    }
}
