package br.ifsp.demo.utils;

import br.ifsp.demo.domain.model.movie.Genre;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.model.movie.MovieId;
import br.ifsp.demo.security.auth.Role;
import br.ifsp.demo.security.auth.User;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.UUID;

public class EntityBuilder {

    private static final Faker faker = Faker.instance();

    public static User createRandomUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name(faker.name().firstName())
                .lastname(faker.name().lastName())
                .email(faker.internet().safeEmailAddress())
                .password("password123")
                .role(Role.USER)
                .ratings(new ArrayList<>())
                .build();
    }

    public static Movie createRandomMovie() {
        return new Movie(
                new MovieId(UUID.randomUUID()),
                faker.book().title(),
                Genre.SCI_FI
        );
    }
}