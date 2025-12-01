package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponsiveUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Recomendações devem ser exibidas corretamente em viewport mobile")
    void shouldRenderRecommendationsCorrectlyOnMobileViewport() {
        // Login com usuário que já possui histórico de avaliações
        RecommendationsPage recommendationsPage =
                loginAsExistingUser("lucas@gmail.com", "senha");

        // Define um viewport típico de mobile (ex.: iPhone X)
        driver.manage().window().setSize(new Dimension(375, 812));

        // Confere que o título ainda está visível
        assertThat(recommendationsPage.getHeaderTitle())
                .as("o título da página deve continuar visível em mobile")
                .contains("Recomendado para Você");

        // Confere que ainda existe ao menos um card de recomendação
        assertThat(recommendationsPage.hasAnyRecommendationCard())
                .as("deve haver filmes recomendados mesmo em viewport mobile")
                .isTrue();
    }
}