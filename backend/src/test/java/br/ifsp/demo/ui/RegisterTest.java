package br.ifsp.demo.ui;

import br.ifsp.demo.ui.utils.BaseTest;
import br.ifsp.demo.ui.utils.pages.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTest extends BaseTest {
    private final String VALID_FIRST_NAME = "Matheus";
    private final String VALID_LAST_NAME = "Gusmão";
    private final String VALID_PASSWORD = "12345678";

    private String email;

    @BeforeEach
    void prepareUniqueEmail() {
        this.email = "user.test+" + System.currentTimeMillis() + "@ifsp.com";
    }

    @Tag("UiTest")
    @Test
    void successfulRegistrationTest() {
        RegisterPage registerPage = new RegisterPage(driver, wait);
        registerPage.open();

        registerPage.performRegister(VALID_FIRST_NAME, VALID_LAST_NAME, email, VALID_PASSWORD);

        waitForUrlContains("/login");

        String toast = registerPage.getToastMessagePart("Registro bem-sucedido!");

        assertThat(toast).isNotEmpty();
    }
}