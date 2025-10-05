package br.ifsp.demo.service.get;

import br.ifsp.demo.domain.user.User;
import br.ifsp.demo.exception.UserNotFoundException;
import br.ifsp.demo.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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

        return new RatedServiceResponseDTO(restoredUser.getRatings());
    }
}
