package br.ifsp.demo.domain.service.get;

import br.ifsp.demo.domain.model.user.User;
import br.ifsp.demo.domain.exception.UserNotFoundException;
import br.ifsp.demo.domain.repository.JpaUserRepository;
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
