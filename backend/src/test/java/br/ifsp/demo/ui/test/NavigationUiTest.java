package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.BaseAuthenticatedUiTest.AuthSession;
import br.ifsp.demo.ui.page.CatalogPage;
import br.ifsp.demo.ui.page.LoginPage;
import br.ifsp.demo.ui.page.MyRatingsPage;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigationUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir navegar entre Recomendações e Catálogo pela navbar")
    void shouldNavigateBetweenRecommendationsAndCatalogUsingNavbar() {
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Recomendações -> Catálogo
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();
        assertThat(catalogPage.getHeaderTitle())
                .as("deve exibir o título do catálogo ao acessar a aba Catálogo")
                .contains("Catálogo Completo");

        // Catálogo -> Recomendações (voltar)
        RecommendationsPage backToRecommendations = catalogPage.goToRecommendationsTab();
        assertThat(backToRecommendations.getHeaderTitle())
                .as("deve exibir novamente o título de recomendações ao voltar para a aba Recomendações")
                .contains("Recomendado para Você");
    }

    @Disabled("Bug conhecido: ao clicar em 'Minhas Avaliações' na aba Catálogo, a URL permanece em /movies em vez de ir para /profile")
    @Test
    @Tag("UiTest")
    @DisplayName("BUG: Deve navegar de Catálogo para Minhas Avaliações pela navbar")
    void shouldNavigateFromCatalogToMyRatingsButBugKeepsOnMovies() {
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Recomendações -> Catálogo
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();

        // Catálogo -> Minhas Avaliações (comportamento ESPERADO, mas hoje bugado)
        MyRatingsPage myRatingsPage = catalogPage.goToMyRatingsTab();

        assertThat(myRatingsPage.getHeaderTitle())
                .as("ao clicar em Minhas Avaliações na aba Catálogo, deveria ir para /profile")
                .contains("Minhas Avaliações");
    }

        @Test
        @Tag("UiTest")
        @DisplayName("Deve permitir realizar logout e voltar para a tela de login")
        void shouldLogoutAndReturnToLoginPage() {
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // só chama o método, não precisa guardar o retorno
        recommendationsPage.clickLogout();

        assertThat(driver.getCurrentUrl())
                .as("após logout, deve voltar para a tela de login")
                .contains("/login");
        }
}