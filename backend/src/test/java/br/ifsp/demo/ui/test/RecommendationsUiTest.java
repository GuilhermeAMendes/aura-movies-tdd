package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecommendationsUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve mostrar recomendações para usuário com histórico de avaliações")
    void shouldShowRecommendationsForUserWithRatings() {
        // Lucas já vem seedado no backend com várias avaliações
        RecommendationsPage recommendationsPage =
                loginAsExistingUser("lucas@gmail.com", "senha");

        // Confere título da página
        assertThat(recommendationsPage.getHeaderTitle())
                .as("o título da página de recomendações deve ser exibido")
                .contains("Recomendado para Você");

        // Confere que há ao menos um card recomendado
        assertThat(recommendationsPage.hasAnyRecommendationCard())
                .as("deve haver ao menos um filme recomendado para usuário com histórico")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve mostrar recomendações para usuário sem avaliações (recomendações genéricas/aleatórias)")
    void shouldShowRecommendationsForUserWithoutRatings() {
        // Alan Turing vem seedado sem avaliações
        RecommendationsPage recommendationsPage =
                loginAsExistingUser("turing@gmail.com", "senha");

        assertThat(recommendationsPage.getHeaderTitle())
                .as("o título da página de recomendações deve ser exibido")
                .contains("Recomendado para Você");

        assertThat(recommendationsPage.hasAnyRecommendationCard())
                .as("mesmo sem avaliações, o usuário deve receber uma lista de filmes (aleatória)")
                .isTrue();
    }
}