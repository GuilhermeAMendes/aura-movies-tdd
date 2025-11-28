package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RecommendationsPage extends BasePageObject {

    private final WebDriverWait wait;

    public RecommendationsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/recommendations"));
    }

    public RecommendationsPage goToRecommendationsTab() {
        driver.findElement(By.linkText("Recomendações")).click();
        return this;
    }

    public CatalogPage goToCatalogTab() {
        driver.findElement(By.linkText("Catálogo")).click();
        return new CatalogPage(driver);
    }

    public MyRatingsPage goToMyRatingsTab() {
        driver.findElement(By.linkText("Minhas Avaliações")).click();
        return new MyRatingsPage(driver);
    }

    public List<WebElement> recommendationCards() {
        // Ajustar seletor pro card de filme (pode ser uma div, article, etc.)
        return driver.findElements(By.cssSelector("[data-test='movie-card']"));
    }
}