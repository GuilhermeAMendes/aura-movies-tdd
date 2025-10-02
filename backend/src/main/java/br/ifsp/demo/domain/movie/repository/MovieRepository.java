package br.ifsp.demo.domain.movie.repository;

import br.ifsp.demo.domain.movie.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
}
