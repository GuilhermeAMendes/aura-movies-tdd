package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseSeleniumTest;
import br.ifsp.demo.ui.page.LoginPage;
import br.ifsp.demo.ui.page.RegisterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationValidationUiTest extends BaseSeleniumTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir mensagens de validação ao tentar registrar com campos vazios")
    void shouldShowValidationErrorsWhenRegisteringWithEmptyFields() {
        // Vai direto para a tela de registro
        driver.get(BASE_URL + "/register");
        RegisterPage registerPage = new RegisterPage(driver);

        // Não preenche nada e tenta registrar
        registerPage.submitWithErrors();

        assertThat(registerPage.isNameRequiredMessageVisible())
                .as("deve exibir mensagem de nome obrigatório")
                .isTrue();

        assertThat(registerPage.isLastNameRequiredMessageVisible())
                .as("deve exibir mensagem de sobrenome obrigatório")
                .isTrue();

        assertThat(registerPage.isEmailInvalidMessageVisible())
                .as("deve exibir mensagem de e-mail inválido")
                .isTrue();

        assertThat(registerPage.isPasswordRequiredMessageVisible())
                .as("deve exibir mensagem de senha obrigatória")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir erro ao tentar fazer login com credenciais inválidas")
    void shouldShowErrorWhenLoginWithInvalidCredentials() {
        // Vai para a tela de login
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);

        // Usa um e-mail qualquer bem formatado, mas inexistente, e senha incorreta
        loginPage
                .typeEmail("usuario.inexistente@email.com")
                .typePassword("senha-errada")
                .submitWithInvalidCredentials();

        assertThat(loginPage.isLoginErrorToastVisible())
                .as("deve exibir toast de erro 'Falha no Login'")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir mensagem de e-mail inválido ao registrar com e-mail mal formatado")
    void shouldShowEmailValidationWhenRegisteringWithInvalidEmailFormat() {
        driver.get(BASE_URL + "/register");
        RegisterPage registerPage = new RegisterPage(driver);

        // Preenche campos obrigatórios com valores válidos,
        // mas usa um e-mail claramente inválido (classe 'formato inválido')
        registerPage
                .typeName("Maria")
                .typeLastName("Silva")
                .typeEmail("maria.sem-arroba") // inválido
                .typePassword("SenhaForte123!");

        registerPage.submitWithErrors();

        assertThat(registerPage.isEmailInvalidMessageVisible())
                .as("deve exibir mensagem de e-mail inválido no cadastro")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir mensagem de e-mail inválido ao tentar login com e-mail mal formatado")
    void shouldShowEmailValidationWhenLoginWithInvalidEmailFormat() {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);

        // E-mail sem formato válido, mas senha preenchida
        loginPage
                .typeEmail("email-invalido")
                .typePassword("qualquerCoisa")
                .submitWithInvalidCredentials();

        assertThat(loginPage.isEmailValidationMessageVisible())
                .as("deve exibir mensagem de e-mail inválido na tela de login")
                .isTrue();
    }
}