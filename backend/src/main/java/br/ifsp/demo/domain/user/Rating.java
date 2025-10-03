package br.ifsp.demo.domain.user;

import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.MovieId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Rating {
    private final UUID id;
    private final MovieId movieId;
    private Grade grade;
    private LocalDateTime lastGradedAt;
}
