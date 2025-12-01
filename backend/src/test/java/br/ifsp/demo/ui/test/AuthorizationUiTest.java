package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseSeleniumTest;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationUiTest extends BaseSeleniumTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir estado vazio ao acessar recomendações sem estar autenticado")
    void shouldShowEmptyStateWhenUnauthenticatedUserAccessesRecommendations() {
        // garante que não tem sessão anterior
        driver.manage().deleteAllCookies();

        // tenta acessar recomendações diretamente
        driver.get(BASE_URL + "/recommendations");

        RecommendationsPage page = new RecommendationsPage(driver);

        // continua em /recommendations (comportamento atual da aplicação)
        assertThat(driver.getCurrentUrl())
                .as("rota continua /recommendations, exibindo estado vazio")
                .contains("/recommendations");

        String alertText = page.getAlertText();

        // comportamento REAL da aplicação hoje
        assertThat(alertText)
                .as("deve exibir mensagem de nenhuma recomendação encontrada")
                .contains("Nenhuma recomendação encontrada");
    }
}