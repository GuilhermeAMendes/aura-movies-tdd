package br.ifsp.demo.ui;

import br.ifsp.demo.ui.utils.BaseTest;
import br.ifsp.demo.ui.utils.pages.LoginPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    private final String VALID_EMAIL = "lucas@gmail.com";
    private final String VALID_PASSWORD = "senha";

    @Tag("UiTest")
    @Test
    void successfulLoginTest() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.performLogin(VALID_EMAIL, VALID_PASSWORD);

        waitForUrlContains("/recommendations");

        final By successToast = By.xpath("//div[contains(text(), 'Login bem-sucedido!')]");

        assertThat(driver.findElement(successToast).isDisplayed()).isTrue();
    }

    @Tag("UiTest")
    @Test
    void loginFailedInvalidCredentialsTest() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.performLogin(VALID_EMAIL, "senha_errada_123");

        final By errorToast = By.xpath("//div[contains(text(), 'Falha no Login')]");

        assertThat(errorToast).isNotNull();

        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Tag("UiTest")
    @ParameterizedTest(name = "Teste {index}: Campo [{2}] deve falhar com erro: {3}")
    @CsvSource({
            "'', senha, email, 'Por favor, insira um e-mail válido.'",
            "email_invalido, senha, email, 'Por favor, insira um e-mail válido.'",
            "lucas@gmail.com, '', password, A senha é obrigatória."
    })
    void loginFailedBecauseValidationTest(String email, String password, String fieldName, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.performLogin(email, password);

        final String actualErrorMessage = loginPage.getFieldErrorMessage(fieldName);

        assertThat(driver.getCurrentUrl()).contains("/login");
        assertThat(actualErrorMessage).isEqualTo(expectedMessage);
    }

    @Tag("UiTest")
    @Test
    void successfulLogoutTest() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.performLogin(VALID_EMAIL, VALID_PASSWORD);

        waitForUrlContains("/recommendations");

        loginPage.performLogout();

        waitForUrlIsEqualTo(FRONT_URL);

        assertThat(driver.getCurrentUrl()).isEqualTo(FRONT_URL);
    }

    @Tag("UiTest")
    @Test
    void whenLoggedInTheAccessButtonAppearsTest() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.performLogin(VALID_EMAIL, VALID_PASSWORD);

        waitForUrlContains("/recommendations");

        driver.navigate().to(FRONT_URL);

        final By accessButton = By.xpath("//button[.//span[normalize-space()='Acessar']]");

        assertThat(driver.findElement(accessButton).isDisplayed()).isTrue();
    }
}