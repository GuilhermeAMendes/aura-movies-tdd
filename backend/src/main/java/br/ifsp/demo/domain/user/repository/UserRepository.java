package br.ifsp.demo.domain.user.repository;

import br.ifsp.demo.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById (UUID id);
}
