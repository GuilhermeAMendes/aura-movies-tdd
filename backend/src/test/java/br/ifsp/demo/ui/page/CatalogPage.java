package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CatalogPage extends BasePageObject {

    private final WebDriverWait wait;

    public CatalogPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // rota do catálogo: /movies
        wait.until(ExpectedConditions.urlContains("/movies"));
    }

    public MovieDetailsPage openFirstMovieCard() {
        // cards são links para /movies/{id}
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href^='/movies/']"))
        ).click();

        return new MovieDetailsPage(driver);
    }

    public RecommendationsPage goToRecommendationsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Recomendações"))
        ).click();
        return new RecommendationsPage(driver);
    }

    public MyRatingsPage goToMyRatingsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Minhas Avaliações"))
        ).click();
        return new MyRatingsPage(driver);
    }

    public String getHeaderTitle() {
        // h1 "Catálogo Completo"
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1")
                )
        ).getText();
    }
}