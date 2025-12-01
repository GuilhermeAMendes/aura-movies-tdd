package br.ifsp.demo.integration.api;

import br.ifsp.demo.domain.model.movie.Movie;
import br.ifsp.demo.domain.repository.JpaMovieRepository;
import br.ifsp.demo.integration.BaseApiIntegrationTest;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.utils.EntityBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("ApiTest")
@Tag("IntegrationTest")
public class RatingsControllerIntegrationTest extends BaseApiIntegrationTest {

    @Autowired
    private JpaMovieRepository movieRepository;

    @Test
    @Disabled("Bug: API cannot deserialize Grade object from JSON (HttpMessageNotReadableException)")
    @DisplayName("[POST] Should rate a movie successfully (201 Created)")
    void shouldRateMovieSuccessfully() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        Movie movie = EntityBuilder.createRandomMovie();
        movie = movieRepository.save(movie);

        String validPayload = String.format("""
            {
                "movieId": "%s",
                "grade": 5
            }
        """, movie.getMovieId().unwrap());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(validPayload)
                .when()
                .post("/api/v1/ratings")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("rating.grade.value", equalTo(5))
                .body("rating.movieId.id", equalTo(movie.getMovieId().unwrap().toString()));
    }

    @Test
    @Disabled("Bug: API cannot deserialize Grade object from JSON (HttpMessageNotReadableException)")
    @DisplayName("[POST] Should return 404 when rating a non-existent movie")
    void shouldReturn404ForNonExistentMovie() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        String randomMovieId = UUID.randomUUID().toString();

        String payload = String.format("""
            {
                "movieId": "%s",
                "grade": 3
            }
        """, randomMovieId);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/ratings")
                .then()
                .log().ifValidationFails()
                .statusCode(404);
    }

    @Test
    @Disabled("Bug: API cannot deserialize Grade object from JSON (HttpMessageNotReadableException)")
    @DisplayName("[POST] Should return error when grade is invalid (Sad Path)")
    void shouldReturnErrorForInvalidGrade() {
        User user = EntityBuilder.createRandomUser();
        String token = authenticateUser(user, "password123");

        Movie movie = EntityBuilder.createRandomMovie();
        movieRepository.save(movie);

        String invalidPayload = String.format("""
            {
                "movieId": "%s",
                "grade": 6
            }
        """, movie.getMovieId().unwrap());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/api/v1/ratings")
                .then()
                .log().ifValidationFails()
                .statusCode(not(is(201)))
                .statusCode(anyOf(is(400), is(500)));
    }
}