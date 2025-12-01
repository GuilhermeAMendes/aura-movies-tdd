package br.ifsp.demo.integration;

import br.ifsp.demo.domain.repository.JpaUserRepository;
import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.security.auth.AuthResponse;
import br.ifsp.demo.security.auth.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;

@Tag("ApiTest")
@Tag("IntegrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected JpaUserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    protected String authenticateUser(User user, String plainTextPassword) {
        user.setPassword(passwordEncoder.encode(plainTextPassword));
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest(user.getEmail(), plainTextPassword);

        AuthResponse response = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract().as(AuthResponse.class);

        return response.token();
    }
}