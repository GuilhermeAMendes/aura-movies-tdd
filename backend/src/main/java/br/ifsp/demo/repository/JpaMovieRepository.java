package br.ifsp.demo.repository;

import br.ifsp.demo.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMovieRepository extends JpaRepository<Movie, Long> {
    // findAll() is inherited! No need to declare.
}
