package br.ifsp.demo.repository;

import br.ifsp.demo.domain.movie.Movie;
import br.ifsp.demo.domain.movie.MovieId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMovieRepository extends JpaRepository<Movie, MovieId> {
    // findAll() is inherited! No need to declare.
}
