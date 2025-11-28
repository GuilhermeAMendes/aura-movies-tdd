package br.ifsp.demo.ui.test;

import br.ifsp.demo.ui.BaseAuthenticatedUiTest;
import br.ifsp.demo.ui.BaseAuthenticatedUiTest.AuthSession;
import br.ifsp.demo.ui.page.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyRatingsUiTest extends BaseAuthenticatedUiTest {

    @Test
    @Tag("UiTest")
    @DisplayName("Deve mostrar mensagem de vazio quando usuário não tem avaliações")
    void shouldShowEmptyMessageWhenUserHasNoRatings() {
        // Usuário novo registrado e logado, sem avaliações ainda
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        MyRatingsPage myRatingsPage = recommendationsPage.goToMyRatingsTab();

        assertThat(myRatingsPage.isEmptyMessageVisible())
                .as("deve exibir a mensagem de nenhuma avaliação encontrada")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve listar filmes avaliados na aba Minhas Avaliações")
    void shouldShowRatedMovieInMyRatings() {
        // Usuário novo registrado e logado
        AuthSession session = registerRandomUserAndLogin();
        RecommendationsPage recommendationsPage = session.recommendationsPage();

        // Ir para o catálogo e avaliar um filme
        CatalogPage catalogPage = recommendationsPage.goToCatalogTab();
        MovieDetailsPage movieDetailsPage = catalogPage.openFirstMovieCard();

        String movieTitle = movieDetailsPage.getMovieTitle();

        int expectedRating = 4;
        movieDetailsPage
                .selectRating(expectedRating)
                .saveRating();

        // Ir para "Minhas Avaliações"
        driver.get(BASE_URL + "/profile");
        MyRatingsPage myRatingsPage = new MyRatingsPage(driver);

        assertThat(myRatingsPage.hasRatingForMovie(movieTitle))
                .as("deve exibir o filme avaliado na lista de avaliações")
                .isTrue();
    }
}