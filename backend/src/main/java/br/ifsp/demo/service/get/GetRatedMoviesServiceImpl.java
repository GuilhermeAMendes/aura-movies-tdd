package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.user.Rating;
import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaUserRepository;

import java.util.List;
import java.util.Optional;

public class GetRatedMoviesServiceImpl implements GetRatedMoviesService {
    private final JpaUserRepository userRepository;

    public GetRatedMoviesServiceImpl(final JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RatedServiceResponseDTO restoreRatedMovies(RatedServiceRequestDTO request) {
        Optional<User> userOptional = userRepository.findUserById(request.userId());

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User restoredUser = userOptional.get();

        if (restoredUser.getRatings().isEmpty()) {
            return new RatedServiceResponseDTO(List.of(new Rating(null, null, null, null)));
        }

        return new RatedServiceResponseDTO(restoredUser.getRatings());
    }
}
