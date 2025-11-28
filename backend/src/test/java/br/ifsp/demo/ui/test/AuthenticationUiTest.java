package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseSeleniumTest;
import br.ifsp.demo.ui.page.HomePage;
import br.ifsp.demo.ui.page.LoginPage;
import br.ifsp.demo.ui.page.RecommendationsPage;
import br.ifsp.demo.ui.page.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationUiTest extends BaseSeleniumTest {

    private final Faker faker = new Faker();

    @Override
    protected void setInitialPage() {
        driver.get(BASE_URL + "/");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir registrar e logar com um novo usuário e exibir recomendações")
    void shouldRegisterAndLoginAndShowRecommendations() {
        // arrange - home
        HomePage home = new HomePage(driver);
        RegisterPage registerPage = home.clickRegister();

        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = "SenhaSegura123";

        // act - cadastrar
        LoginPage loginPage = registerPage
                .typeName(name)
                .typeLastName(lastName)
                .typeEmail(email)
                .typePassword(password)
                .submitRegister();

        // act - login
        RecommendationsPage recommendationsPage = loginPage
                .typeEmail(email)
                .typePassword(password)
                .submitValidLogin()
                .goToRecommendationsTab();

        // assert - deve ter recomendações na tela
        assertThat(recommendationsPage.recommendationCards())
                .as("lista de recomendações deve ser exibida")
                .isNotEmpty();
    }
}