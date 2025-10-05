package br.ifsp.demo.domain.user;

import br.ifsp.demo.domain.movie.Grade;
import br.ifsp.demo.domain.movie.MovieId;
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