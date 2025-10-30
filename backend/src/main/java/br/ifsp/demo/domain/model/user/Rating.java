package br.ifsp.demo.domain.model.user;

import br.ifsp.demo.domain.model.movie.Grade;
import br.ifsp.demo.domain.model.movie.MovieId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Embedded
    private MovieId movieId;

    @Embedded
    private Grade grade;

    private LocalDateTime lastGradedAt;
}