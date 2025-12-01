package br.ifsp.demo.ui;

import br.ifsp.demo.ui.utils.BaseTest;
import br.ifsp.demo.ui.utils.pages.LoginPage;
import br.ifsp.demo.ui.utils.pages.movies.CatalogPage;
import br.ifsp.demo.ui.utils.pages.movies.MovieDetailPage;
import br.ifsp.demo.ui.utils.pages.MyRatingsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingLifecycleTest extends BaseTest {

    @BeforeEach
    void setupAndLogin() {
        LoginPage loginPage = new LoginPage(driver, wait);
        String USER_EMAIL = "turing@gmail.com";
        String USER_PASS = "senha";
        loginPage.performLogin(USER_EMAIL, USER_PASS);
    }

    @Tag("UiTest")
    @Test
    void shouldCreateAndEditRatingSuccessfully() {
        CatalogPage catalogPage = new CatalogPage(driver, wait);
        MovieDetailPage detailPage = new MovieDetailPage(driver, wait);
        MyRatingsPage myRatingsPage = new MyRatingsPage(driver, wait);

        catalogPage.navigateToCatalog();
        catalogPage.clickFirstMovie();

        wait.until(ExpectedConditions.urlContains("/movies/"));

        String targetMovieTitle = detailPage.getMovieTitle().trim();
        System.out.println("Filme selecionado para teste: [" + targetMovieTitle + "]");

        int initialRating = 3;
        detailPage.rateMovie(initialRating);

        myRatingsPage.navigateToMyRatings();

        wait.until(ExpectedConditions.urlContains("/profile"));

        int savedRating = myRatingsPage.getRatingForMovie(targetMovieTitle);

        assertThat(savedRating)
                .as("A avaliação salva em '/profile' para o filme '%s' deve ser 3", targetMovieTitle)
                .isEqualTo(initialRating);

        myRatingsPage.clickMovieToEdit(targetMovieTitle);

        wait.until(ExpectedConditions.urlContains("/movies/"));
        assertThat(detailPage.getMovieTitle().trim()).isEqualTo(targetMovieTitle);

        int newRating = 5;
        detailPage.rateMovie(newRating);

        myRatingsPage.navigateToMyRatings();

        int updatedRating = myRatingsPage.getRatingForMovie(targetMovieTitle);

        assertThat(updatedRating)
                .as("A avaliação deve ter sido atualizada para 5 estrelas após a edição")
                .isEqualTo(newRating);

        String deleteButtonXpath = String.format(
                "//div[contains(@class, 'cursor-pointer')][.//p[contains(., \"%s\")]]//button[.//svg[contains(@class, 'lucide-trash')]]",
                targetMovieTitle.trim()
        );

        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(deleteButtonXpath)));
        deleteButton.click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                        "//div[contains(@class, 'cursor-pointer')][.//p[contains(., \"%s\")]]",
                        targetMovieTitle.trim()
                ))),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nenhuma avaliação encontrada')]"))
        ));

        boolean ratingStillExists;
        try {
            new WebDriverWait(driver, java.time.Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                            "//div[contains(@class, 'cursor-pointer')][.//p[contains(., \"%s\")]]",
                            targetMovieTitle.trim()
                    ))));
            ratingStillExists = true;
        } catch (Exception e) {
            ratingStillExists = false;
        }

        assertThat(ratingStillExists)
                .as("A avaliação do filme '%s' deve ter sido removida após o delete", targetMovieTitle)
                .isFalse();
    }

    // TODO:
    // 1. CRIAR CASO E TESTE PARA DELETAR DIRETO PELO MOVIE DETAIL CARD E NÃO PELOS RATINGS
}