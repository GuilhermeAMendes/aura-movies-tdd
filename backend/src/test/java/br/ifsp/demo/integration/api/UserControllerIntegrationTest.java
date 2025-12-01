package br.ifsp.demo.integration.api;

import br.ifsp.demo.integration.BaseApiIntegrationTest;
import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.security.auth.RegisterUserRequest;
import br.ifsp.demo.security.auth.User;
import br.ifsp.demo.utils.EntityBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserControllerIntegrationTest extends BaseApiIntegrationTest {

    @Test
    @DisplayName("[POST] Should register new user successfully (201 Created)")
    void shouldRegisterUserSuccessfully() {
        User user = EntityBuilder.createRandomUser();
        RegisterUserRequest request = new RegisterUserRequest(
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                "senhaForte123"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/register")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("[POST] Should return 409 Conflict when email already exists")
    void shouldReturnConflictWhenEmailExists() {
        User existingUser = EntityBuilder.createRandomUser();
        existingUser.setPassword(passwordEncoder.encode("senha123"));
        userRepository.save(existingUser);

        RegisterUserRequest request = new RegisterUserRequest(
                "Novo Nome", "Novo Sobrenome",
                existingUser.getEmail(), // Email duplicado
                "senha123"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/register")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("[POST] Should authenticate successfully and return Token")
    void shouldAuthenticateSuccessfully() {
        User user = EntityBuilder.createRandomUser();
        String rawPassword = "password123";
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest(user.getEmail(), rawPassword);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("[POST] Should fail authentication with wrong password (401)")
    void shouldFailAuthWithWrongPassword() {
        User user = EntityBuilder.createRandomUser();
        user.setPassword(passwordEncoder.encode("senhaCorreta"));
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest(user.getEmail(), "senhaErrada");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(401);
    }
}