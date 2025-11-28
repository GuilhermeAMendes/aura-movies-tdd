package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CatalogPage extends BasePageObject {

    private final WebDriverWait wait;

    public CatalogPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // URL do catálogo: /movies
        wait.until(ExpectedConditions.urlContains("/movies"));
    }

    public MovieDetailsPage openFirstMovieCard() {
        // Pega o primeiro link que leva para /movies/{id}
        WebElement firstMovieLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("a[href^='/movies/']")
                )
        );
        firstMovieLink.click();
        return new MovieDetailsPage(driver);
    }
}
