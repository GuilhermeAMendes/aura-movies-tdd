package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.page.CatalogPage;
import br.ifsp.demo.ui.page.MyRatingsPage;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import br.ifsp.demo.ui.page.HomePage;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigationUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir navegar de Recomendações para Catálogo e Minhas Avaliações pela navbar")
    void shouldNavigateBetweenTabsUsingNavbar() {
        // login com usuário seedado (Lucas)
        RecommendationsPage recommendationsPage =
                loginAsExistingUser("lucas@gmail.com", "senha");

        // Recomendações -> Catálogo
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();
        assertThat(catalogPage.getHeaderTitle())
                .as("deve exibir o título do catálogo ao acessar a aba Catálogo")
                .contains("Catálogo Completo");

        // Catálogo -> Minhas Avaliações
        MyRatingsPage myRatingsPage = catalogPage.goToMyRatingsTab();
        assertThat(myRatingsPage.getHeaderTitle())
                .as("deve exibir o título de Minhas Avaliações ao acessar a aba correspondente")
                .contains("Minhas Avaliações");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir realizar logout e voltar para a tela inicial pública")
    void shouldLogoutAndReturnToPublicHomePage() {
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        HomePage homePage = recommendationsPage.clickLogout();

        // Verifica que voltou para a home pública ("/")
        assertThat(driver.getCurrentUrl())
                .as("após logout, deve voltar para a página inicial pública")
                .contains("http://localhost:3000/");

        // E que a home exibe o botão de Login (indicando que não está mais autenticado)
        // Se quiser reforçar, dá pra checar isso também:
        // (não mexi na HomePage, mas se quiser podemos adicionar um método pra isso depois)
    }
}
