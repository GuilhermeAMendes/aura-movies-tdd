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

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir alterar a nota de um filme já avaliado")
    void shouldAllowUserToEditExistingRating() {
        // Usuário novo registrado e logado
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Ir para o catálogo e avaliar um filme
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();
        MovieDetailsPage movieDetailsPage = catalogPage.openFirstMovieCard();

        // Avaliação inicial
        int initialRating = 2;
        movieDetailsPage
                .selectRating(initialRating)
                .saveRating();

        assertThat(movieDetailsPage.getDisplayedRatingValue())
                .as("a nota inicial deve ser registrada")
                .isEqualTo(initialRating);

        // Alterar a nota
        int newRating = 5;
        movieDetailsPage
                .clickEditRating()
                .selectRating(newRating)
                .saveRating();

        int actualRating = movieDetailsPage.getDisplayedRatingValue();

        assertThat(actualRating)
                .as("a nova nota deve substituir a nota anterior")
                .isEqualTo(newRating);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir remover a avaliação de um filme na tela de detalhes")
    void shouldAllowUserToDeleteRatingFromMovieDetails() {
        // Usuário novo registrado e logado
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Ir para o catálogo e avaliar um filme
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();
        MovieDetailsPage movieDetailsPage = catalogPage.openFirstMovieCard();

        int rating = 4;
        movieDetailsPage
                .selectRating(rating)
                .saveRating();

        assertThat(movieDetailsPage.getDisplayedRatingValue())
                .as("a nota deve ser registrada antes de remover")
                .isEqualTo(rating);

        // Remover a avaliação
        movieDetailsPage.clickDeleteRating();

        // Após remover, o formulário de rating deve voltar a aparecer (botões 0-5)
        assertThat(movieDetailsPage.isRatingFormVisible())
                .as("o formulário de avaliação deve voltar a aparecer após remover a nota")
                .isTrue();
    }
}