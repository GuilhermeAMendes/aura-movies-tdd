package br.ifsp.demo.ui;

import br.ifsp.demo.ui.utils.BaseTest;
import br.ifsp.demo.ui.utils.pages.movies.CatalogPage;
import br.ifsp.demo.ui.utils.pages.LoginPage;
import br.ifsp.demo.ui.utils.pages.movies.MovieDetailPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CatalogAndMovieTest extends BaseTest {
    // usuário que não possuí nenhuma avaliação.
    private final String USER_EMAIL = "turing@gmail.com";
    private final String USER_PASS = "senha";

    @BeforeEach
    void setupAndLogin() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.performLogin(USER_EMAIL, USER_PASS);
    }

    @Tag("UiTest")
    @Test
    void shouldDisplayCatalogWithMoviesTest() {
        CatalogPage catalogPage = new CatalogPage(driver, wait);

        waitForUrlContains("/recommendations");

        catalogPage.navigateToCatalog();

        assertThat(catalogPage.isCatalogLoaded())
                .as("A página de catálogo deve carregar corretamente")
                .isTrue();

        int movieCount = catalogPage.getMoviesCount();
        assertThat(movieCount)
                .as("O catálogo deve conter pelo menos um filme")
                .isGreaterThan(0);
    }

    @Tag("UiTest")
    @Test
    void shouldNavigateToMovieDetailsAndShowInfoTest() {
        CatalogPage catalogPage = new CatalogPage(driver, wait);
        MovieDetailPage movieDetailPage = new MovieDetailPage(driver, wait);

        catalogPage.navigateToCatalog();

        waitForUrlContains("/movies");

        catalogPage.clickFirstMovie();

        String title = movieDetailPage.getMovieTitle();
        String genre = movieDetailPage.getMovieGenre();

        assertThat(title)
                .as("O título do filme não deve ser vazio")
                .isNotBlank();

        assertThat(genre)
                .as("O gênero do filme deve ser exibido")
                .isNotBlank();

        assertThat(movieDetailPage.isRatingFormVisible())
                .as("O formulário de avaliação deve estar visível para usuário logado")
                .isTrue();
    }
}