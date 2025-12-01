package br.ifsp.demo.ui;

import br.ifsp.demo.ui.page.*;
import com.github.javafaker.Faker;

public abstract class BaseAuthenticatedUiTest extends BaseSeleniumTest {

    protected final Faker faker = new Faker();

    @Override
    protected void setInitialPage() {
        // Todo teste que herdar desta classe começa na home
        driver.get(BASE_URL + "/");
    }

    /**
     * Registra um novo usuário com dados aleatórios e já realiza o login.
     * Retorna um AuthSession contendo credenciais e a RecommendationsPage.
     */
    protected AuthSession registerRandomUserAndLogin() {
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = homePage.clickRegister();

        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = "SenhaSegura123";

        LoginPage loginPage = registerPage
                .typeName(name)
                .typeLastName(lastName)
                .typeEmail(email)
                .typePassword(password)
                .submitRegister();

        RecommendationsPage recommendationsPage = loginPage
                .typeEmail(email)
                .typePassword(password)
                .submitValidLogin();

        return new AuthSession(name, lastName, email, password, recommendationsPage);
    }

    // Faz login com um usuário já existente (ex: lucas@gmail.com / senha).
    protected RecommendationsPage loginAsExistingUser(String email, String password) {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);

        return loginPage
                .typeEmail(email)
                .typePassword(password)
                .submitValidLogin();
    }

    // Representa o contexto autenticado: credenciais + página inicial logada.
    public static class AuthSession {
        private final String name;
        private final String lastName;
        private final String email;
        private final String password;
        private final RecommendationsPage recommendationsPage;

        public AuthSession(String name,
                           String lastName,
                           String email,
                           String password,
                           RecommendationsPage recommendationsPage) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.recommendationsPage = recommendationsPage;
        }

        public String name() { return name; }
        public String lastName() { return lastName; }
        public String email() { return email; }
        public String password() { return password; }
        public RecommendationsPage recommendationsPage() { return recommendationsPage; }
    }
}