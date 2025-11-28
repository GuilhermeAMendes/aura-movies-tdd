package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.BaseAuthenticatedUiTest.AuthSession;
import br.ifsp.demo.ui.page.CatalogPage;
import br.ifsp.demo.ui.page.MovieDetailsPage;
import br.ifsp.demo.ui.page.RecommendationsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir que um usuário avalie um filme pelo catálogo")
    void shouldAllowUserToRateMovieFromCatalog() {
        // Usuário novo registrado e logado
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Ir para o catálogo
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();

        // Abrir primeiro filme e avaliar
        MovieDetailsPage movieDetailsPage = catalogPage.openFirstMovieCard();

        int expectedRating = 4;
        movieDetailsPage
                .selectRating(expectedRating)
                .saveRating();

        int actualRating = movieDetailsPage.getDisplayedRatingValue();

        assertThat(actualRating)
                .as("a nota exibida deve ser igual à nota escolhida")
                .isEqualTo(expectedRating);
    }
}