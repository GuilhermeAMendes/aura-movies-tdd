package br.ifsp.demo.integration.api;

import br.ifsp.demo.integration.BaseApiIntegrationTest;
import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.utils.EntityBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@Tag("ApiTest")
@Tag("IntegrationTest")
public class MovieControllerIntegrationTest extends BaseApiIntegrationTest {

    @Autowired
    private JpaMovieRepository movieRepository;

    @Test
    @DisplayName("[GET] Should return all movies (200 OK)")
    void shouldReturnAllMovies() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        Movie m1 = EntityBuilder.createRandomMovie();
        Movie m2 = EntityBuilder.createRandomMovie();
        movieRepository.save(m1);
        movieRepository.save(m2);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/movies")
                .then()
                .statusCode(200)
                .body("movies", hasSize(greaterThanOrEqualTo(2)))
                .body("movies.title", hasItems(m1.getTitle(), m2.getTitle()));
    }

    @Test
    @DisplayName("[GET] Should return movie by ID (200 OK)")
    void shouldReturnMovieById() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        Movie movie = EntityBuilder.createRandomMovie();
        movie = movieRepository.save(movie);

        String movieIdStr = movie.getMovieId().unwrap().toString();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/movies/{id}", movieIdStr)
                .then()
                .statusCode(200)
                .body("movie.title", equalTo(movie.getTitle()))
                .body("movie.genre", equalTo(movie.getGenre().name()));
    }

    @Test
    @DisplayName("[GET] Should return 404 when movie not found")
    void shouldReturn404WhenMovieNotFound() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        String randomId = java.util.UUID.randomUUID().toString();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/movies/{id}", randomId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("[GET] Should return 403/401 when not authenticated")
    void shouldBlockUnauthenticatedRequest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/movies")
                .then()
                .statusCode(anyOf(is(401), is(403)));
    }
}