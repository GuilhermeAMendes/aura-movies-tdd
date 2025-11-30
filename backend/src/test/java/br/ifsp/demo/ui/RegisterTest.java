package br.ifsp.demo.ui;

import br.ifsp.demo.ui.utils.BaseTest;
import br.ifsp.demo.ui.utils.pages.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;

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

        registerPage.performRegister(VALID_FIRST_NAME, VALID_LAST_NAME, email, VALID_PASSWORD);

        waitForUrlContains("/login");

        final By successToast = By.xpath("//div[contains(text(), 'Registro bem-sucedido!')]");

        assertThat(successToast).isNotNull();
    }

    @Tag("UiTest")
    @ParameterizedTest(name = "Teste {index}: Campo [{4}] deve falhar com erro: {5}")
    @CsvSource({
            "'', gusmao, a@b.com, 123, name, O nome é obrigatório",
            "matheus, '', a@b.com, 123, lastname, O sobrenome é obrigatório",
            "matheus, gusmao, '', 123, email, 'Por favor, insira um e-mail válido.'",
            "matheus, gusmao, a@b.com, '', password, A senha é obrigatória"
    })
    void registrationFailedBecauseEmptyFieldsTest(String firstName, String lastName, String email, String password, String fieldName, String expectedMessage) {
        RegisterPage registerPage = new RegisterPage(driver, wait);

        registerPage.performRegister(firstName, lastName, email, password);

        final String erroMessage = registerPage.getFieldErrorMessage(fieldName);

        assertThat(driver.getCurrentUrl()).contains("register");
        assertThat(erroMessage).isEqualTo(expectedMessage);
    }
}